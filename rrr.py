# код к доп заданию лаб 1 по инф
def perevodss(chislo):
    chislostr = str(chislo)
    dlinachisla = len(chislostr)
    spisokfib = []
    a, b = 1, 2
    while len(spisokfib) < dlinachisla:
        spisokfib.append(a)
        a, b = b, a + b
    otv = 0
    c2 = 0
    for i in chislostr[::-1]:
        c2 += 1
        if i == '1':
            otv += spisokfib[c2 - 1]
    print(otv)
perevodss(int(input()))

#--лаба 3 по инфе
# ----1----
#515 -> [</
# примеры
# [</[</[</ -> 3
# рьоа[</лвоы[</ -> 2
# [<//</[</[</ -> 3
# [</jggjcjsd[</[</ldks[</js -> 4
# hkkgukl -> 0
import re
pattern = "\[</"
string = input()
print(len(re.findall(pattern, string)))

#  ---2---
# Аист летит над рекой
# Медведь упал в реку
# Заяц и лиса бегут
# Кривошеее существо гуляет по парку
# яичко упало со стола
import re
string=input()
pat=r"\b\w*[аеёиоуыэюя]{2}\w*(?!\s+\b(?:[аеёиоуыэюя]*[бвгджзйклмнпрстфхцчшщ][аеёиоуыэюя]*){4,})\b"
mac=re.findall(pat,string,re.IGNORECASE)
print(mac)

#---3---
import re
text = []
while True:
    s = input()
    if s:
        text.append(s)
    else:
        break
string = r'(\w+ (\w\.)(\2) P0000)'
for i in text:
    s=re.sub(string, '', i)
    y = i.split()
    if y[0][0] != y[1][0]:
        print(i)
    if s!='0 ':
        print(s)




# Иванов И.И. P0000
# Петров П.П. P0000
# Сидоров С.С. P0000
# Кузнецов К.Н. P0000
# Морозов С.С. P0000
#
# Васильев В.П. P0000
# Семенов С.С. P0000
# Егоров Е.А. P0000
# Федоров Ф.С. P0000
#
# Григорьев Г.О. P0000
# Лебедев Л.А. P0000
# Ковалев К.Н. P0000
# Алексеев А.С. P0000
# Николаев Н.И. P0000
#
# Сергеев С.С. P0000
# Трофимов Т.А. P0000
# Дмитриев Д.Д. P0000
# Смирнов С.В. P0000
# Шестаков Ш.А. P0000
#
# Зайцев З.Н. P0000
# Баранов Б.Н. P0000
# Андреев А.А. P0000
# Михайлов М.В. P0000
# Соловьев С.А. P0000
#
#
# ?: просто не создает группу захвата. Например, a(?:b) будет соответствовать "ab" в "abc".
# Буква «r» в регулярных выражениях Python означает «сырая строка».
# Она нужна для того, чтобы символ «\» не вызывал экранирование символов


x = ['I', 'like', 'to', 'study', 'at', 'ITMO']
print(x[4::-2])
s = input()
sinv = ['r1', 'r2', 'i1', 'r3', 'i2', 'i3', 'i4']
m = {'0', '1'}
if len(s) != 7 or [x for x in s if x not in m]:
    print('Сообщение должно состоять из 7 цифр и содержать только 0 или 1')
else:
    arr = [int(x) for x in list(s)]
    summa1 = (arr[0] + arr[2] + arr[4] + arr[6]) % 2
    summa2 = (arr[1] + arr[2] + arr[5] + arr[6]) % 2
    summa3 = (arr[3] + arr[4] + arr[5] + arr[6]) % 2
    summa = [summa1,summa2,summa3]
    a1=[[1,0,0],[0,1,0],[1,1,0],[0,0,1],[1,0,1],[0,1,1],[1,1,1]]
    for i in range(len(a1)):
        if a1[i]==[summa1,summa2,summa3]:
            print(f'ошибка в символе: {sinv[i]}')
            if arr[i]==0:
                arr[i]=1
            else:
                arr[i]=1
    print('Правильное сообщение:', arr[2],arr[4],arr[5],arr[6])

x = ['I', 'like', 'to', 'study', 'at', 'ITMO']
print(x[:4] + x[4:] == x)

