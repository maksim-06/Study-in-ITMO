def task():
    xml_file = open('файл с расписанием.xml', 'r', encoding='utf-8')
    yaml_file = open('yaml расписание.txt', 'w', encoding='utf-8')
    for line in xml_file:
        xml_string = line
        yaml_string = ''
        c1=0
        count=xml_string.count('>')
        xml_string = xml_string.replace('<', '', 1)
        xml_string = xml_string[::-1].replace('>', '', 1)
        xml_string = xml_string[::-1]
        if '/' in xml_string and count>1:
            c1=xml_string.index('/')
            xml_string=xml_string[:c1]
            p1=xml_string.index('>')
            p2=xml_string.index('<')
            text=xml_string[p1+1:p2]
            yaml_string=xml_string[:p1]+': '+text
        if count<2:
            if '/' not in xml_string:
                p3=xml_string.index('\n')
                yaml_string=xml_string[:p3]+': '
            else:
                continue
        yaml_file.write(yaml_string+'\n' if yaml_string else '')
task()
