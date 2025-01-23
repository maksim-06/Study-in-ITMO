# вариант 17
# 17 XML YAML Среда, суббота

def task():
    xml_file = open('файл с расписанием.xml', 'r', encoding='utf-8')
    yaml_file = open('yaml расписание.txt', 'w', encoding='utf-8')
    for line in xml_file:
        xml_string = line
        yaml_string = ''
        for i in range(len(xml_string)):
            symbol = xml_string[i]
            if symbol == '\t':
                yaml_string += '\t'
            elif symbol == ' ':
                yaml_string += ' '
            elif symbol == '<':
                if xml_string[i + 1] != '/':
                    if xml_string[i+1:i+5] == 'week' and xml_string[i+5] != 's':
                        yaml_string += '- '
                else:
                    break
            elif symbol.isalpha() or symbol.isdigit() or symbol in '.:-':
                yaml_string += symbol
            elif symbol == '>':
                yaml_string += ': '
        yaml_file.write(yaml_string + '\n')


task()