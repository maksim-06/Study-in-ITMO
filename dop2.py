# вариант 17
# 17 XML YAML Среда, суббота

import re

def task2():
    xml_file = open('файл с расписанием.xml', 'r', encoding='utf-8')
    yaml_file = open('yaml расписание.txt', 'w', encoding='utf-8')

    for line in xml_file:
        xml_string = line
        yaml_string = ''
        patt = re.findall(r'\s{0,}<\w{1,}>', xml_string)
        if patt:
            name = patt[0]
            name = name.replace('<', '')
            name = name.replace('>', '')
            if name[0] == '\t':
                num = name.count('\t')
                name = num * '\t' + '-' + name[num:]
            yaml_string += name + ': '
        patt = re.findall(r'>(.*)<', xml_string)
        yaml_string += (patt[0] if patt else '')
        yaml_file.write(yaml_string + '\n' if yaml_string else '')
task2()