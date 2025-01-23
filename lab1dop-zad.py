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
perevodss(10100010)
