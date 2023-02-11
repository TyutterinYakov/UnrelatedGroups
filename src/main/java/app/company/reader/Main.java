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

//    private static final Map<Long, Set<String>> linesByGroup = new HashMap<>();
    private static final Map<String, Line> allLines = new HashMap<>();


    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^(\")[\\d]*(\")$");
        Map<String, List<Element>> elementsByValue = new HashMap<>();
        Map<Element, Group> groupByElement = new HashMap<>();
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
                boolean validLine = true;
                for (String lineElement : elementsFromFile) {
                    if (!pattern.matcher(lineElement).matches()) {
                        validLine = false;
                        break;
                    }
                }
                if (!validLine) {
                    continue;
                }
                int countColumn = 0;
                Group newGroup = new Group(countGroup);
                Line line = new Line(lineFromFile, newGroup);
                allLines.put(lineFromFile, line);
//                Set<String> lines = new HashSet<>();
//                linesByGroup.put(newGroup.getNumber(), lines);
//                lines.add(lineFromFile);

                Element element;
                for (String elementFromFile : elementsFromFile) {
                    if (!elementFromFile.equals("\"\"")) {

                        List<Element> findElements = elementsByValue.get(elementFromFile);
                        if (findElements != null) {
                            int finalCountColumn = countColumn;
                            Optional<Element> maybe = findElements.stream().filter(e ->
                                    e.getColumn() == finalCountColumn)
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


                        Group group = groupByElement.get(element);
                        if (group != null && group.getNumber() != newGroup.getNumber()) {
                            mergedGroup.add(group);
                        } else {
                            groupByElement.put(element, newGroup);
                        }
                    }
                    countColumn++;
                }
                for (Group group : mergedGroup) {
//                    Set<String> remove = linesByGroup.remove(group.getNumber());
//                    linesByGroup.get(newGroup.getNumber()).addAll(remove);
                    group.setNumber(newGroup.getNumber());
                }
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
