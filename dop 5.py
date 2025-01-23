# xml -> wml

xml_file = open('файл с расписанием.xml', 'r', encoding='utf-8')
wml_file = open('расписание.txt', 'w', encoding='utf-8')

wml_file.write('<wml>\n')
wml_file.write('  <card>\n')

for line in xml_file:
    xml_string = line
    wml_string = ''
    if xml_string.strip() == '<?xml version="1.0" encoding="UTF-8"?>' or xml_string.strip() == '<root>':
        continue
    else:
        for i in range(len(xml_string)):
            symbol = xml_string[i]
            if symbol == '\t':
                wml_string += '\t'
            elif symbol == ' ':
                wml_string += ' '
            elif symbol == '<':
                if xml_string[i + 1] != '/':
                    wml_string += '<p>'
                else:
                    break
            elif symbol.isalpha() or symbol.isdigit() or symbol in '.:-':
                wml_string += symbol
            elif symbol == '>':
                wml_string += ': '
        if len(wml_string.strip())==0:
            continue
        else:
            wml_file.write(f'   {wml_string}</p>\n')

wml_file.write('  </card>\n')
wml_file.write('</wml>')