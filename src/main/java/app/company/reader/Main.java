package app.company.reader;

import app.company.reader.model.Element;
import app.company.reader.model.Group;
import app.company.reader.model.Line;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;


public class Main {

    private static final Pattern pattern = Pattern.compile("^(\")[\\d]*(\")$");
    private static final Map<String, Line> allLines = new HashMap<>();
    private static final Map<String, List<Element>> elementsByValue = new HashMap<>();
    private static final Map<Element, Group> groupByElement = new HashMap<>();


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            int countGroup = 0;
            Set<Group> mergedGroup = new HashSet<>();
            while (br.ready()) {
                String lineFromFile = br.readLine();
                if (allLines.containsKey(lineFromFile)) {
                    continue;
                }
                String[] elementsFromFile = lineFromFile.split(";");
                boolean validLine = validateLine(elementsFromFile);
                if (!validLine) {
                    continue;
                }
                int countColumn = 0;
                Group newGroup = new Group(countGroup);
                Line line = new Line(lineFromFile, newGroup);
                allLines.put(lineFromFile, line);

                Element element;
                for (String elementFromFile : elementsFromFile) {
                    if (!elementFromFile.equals("\"\"")) {
                        element = findElementOrCreate(countColumn, elementFromFile);
                        findElementGroupOrAdd(mergedGroup, newGroup, element);
                    }
                    countColumn++;
                }
                mergedGroups(mergedGroup, newGroup);
                mergedGroup.clear();
                countGroup++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        writer();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/1000;
        System.out.println("Время выполнения(c): " + duration);
    }

    //Валидация строки
    private static boolean validateLine(String[] elementsFromFile) {
        for (String lineElement : elementsFromFile) {
            if (!pattern.matcher(lineElement).matches()) {
                return false;
            }
        }
        return true;
    }

    /*Поиск группы элемента. Если найдена - добавляем в коллекцию для дальнейшего слияния.
    * Если нет - привязываем новую новую группу к элементу*/
    private static void findElementGroupOrAdd(Set<Group> mergedGroup, Group newGroup, Element element) {
        Group group = groupByElement.get(element);
        if (group != null) {
            mergedGroup.add(group);
        } else {
            groupByElement.put(element, newGroup);
        }
    }

    /*Ищем элементы с переданным значением из файла(elementFromFile). Если находим - ищем среди них его с такой же
    * позицией в строке, иначе создаем новый и сохраняем */
    private static Element findElementOrCreate(int countColumn, String elementFromFile) {
        Element element;
        List<Element> findElements = elementsByValue.get(elementFromFile);
        if (findElements != null) {
            Optional<Element> maybe = findElements.stream().filter(e ->
                    e.getColumn() == countColumn)
                    .findFirst();
            if (maybe.isPresent()) {
                element = maybe.get();
            } else {
                element = new Element(elementFromFile, countColumn);
                findElements.add(element);
            }
        } else {
            List<Element> elements = new LinkedList<>();
            element = new Element(elementFromFile, countColumn);
            elements.add(element);
            elementsByValue.put(elementFromFile, elements);
        }
        return element;
    }

    //Слияние групп в одну
    private static void mergedGroups(Set<Group> mergedGroup, Group newGroup) {
        for (Group group : mergedGroup) {
            group.setNumber(newGroup.getNumber());
        }
    }

    //Запись в файл
    private static void writer() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"))) {
            StringBuilder sb = new StringBuilder();

            Map<Group, Set<Line>> collect = allLines.values().stream().collect(groupingBy(Line::getGroup, toSet()));

            List<Set<Line>> lines = collect.values().stream().sorted((o1, o2) ->
                    Integer.compare(o2.size(), o1.size())).collect(Collectors.toList());


            long countGroup = lines.stream().filter((c) -> c.size() > 1).count();

            System.out.println("Количество групп с количеством строк больше 1: " + countGroup);

            int count = 0;
            for (Set<Line> lin : lines) {
                sb.append("Группа ").append(++count).append("\n");
                for (Line line : lin) {
                    sb.append(line.getValue()).append("\n");
                }
                if (sb.length()/5000 > 2) {
                    bw.write(sb.toString());
                    sb.setLength(0);
                }
            }
            bw.write(sb.toString());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }





}
