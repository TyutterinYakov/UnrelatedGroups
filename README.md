Первым действием производим считывание строки из файла и проверяем, что не присутствует ли она в 
уже считанном наборе строк, если существует - пропускаем. После чего проверяем ее 
элементы на то, что они корректны, то есть соответствуют формату, если хотя бы один 
некорректен - пропускаем строку целиком. Если все хорошо - создаем новую группу, а если точнее, 
то непересекающееся множество, к которой привязываем считанную из файла строку. Группа будет служить
роли указателя, ее уникальность определяется уникальным номер каждой группы. Сохраняем строку в мапу,
где ключом является String - значение строки, а значение - сама строка, ссылка на объект класса Line. 
Итерируемся по элементам строки, проверяя, что элемент не равен пустому элементу "\"\"" - если так,
пропускаем, но номер колонки все равно инкрементируем, так как для сопоставления строк важна корректная 
информация о расположении элементов. Далее, если условие выше true, двигаемся дальше и пытаем получить
элемент по его строковому представлению(значению) из мапы, где ключом выступает значение элемента, 
а значением - коллекция элементов(объектов класса Element, который хранит в себе позицию(колонку)  
в строке и текстовое представление). Таким образом - если элемент с такой же значением найден, 
проходим по коллекции элементов и находим среди них элемент, с позицией, как у того, что получен из 
файла. Если найден - возвращаем его, если нет - создаем новый. Если элемент с таким значением еще
не встречался - добавляем новую запись в мапу, не забывая сохранить в нее новый элемент. После этого 
производим поиск в мапе, где ключом является элемент(Element), а значением - группа(Group).
Если группа у ранее найденного(созданного) элемента есть - значит мы добавляем ее в список mergedGroup,
по которому после итерации по всем элементам строки мы пройдем и произведем слияние старых групп в новую,
путем изменения значения number во всех объектах Group. После этого с помощью стрима группируем строки
по группам, на основе той, что указана в Line. Группы являются равными, если их number's равны.
Производим сортировку по убыванию количества строк в группах. После этого подсчитываем, в скольких 
группах содержится более одной строки. Из минусов могу отметить, что, возможно, группировку можно 
было бы производить сразу, но неизвестно, насколько это будет эффективнее, так как никакой вложенности
при группировке строк нет и поиск, в целом, осуществляется достаточно быстро. Можно было хранить группы 
сразу в отсортированном виде, но тогда поиск нужной группы будет осуществляться дольше, так как хранить 
мы их будем, например, в TreeSet. Плюс алгоритма в том, что он не растягивается в глубину, а лишь в ширину.


