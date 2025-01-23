import xmltodict
import yaml


def task1():
    with open('файл с расписанием.xml', encoding='utf-8') as xml_file:
        data = xmltodict.parse(xml_file.read())

    with open('yaml расписание.txt', 'w', encoding='utf-8') as yaml_file:
        yaml.dump(data, yaml_file, allow_unicode=True, sort_keys=False)
task1()

