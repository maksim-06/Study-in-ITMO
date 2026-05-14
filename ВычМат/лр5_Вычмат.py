import math

from matplotlib import pyplot as plt


def read_data():
    print("\nВыберите способ ввода:")
    print("1 — вручную, 2 — из файла, 3 — по функции")
    choice = input("> ").strip()

    if choice == "1":
        print("Введите X через пробел:")
        x = list(map(float, input("X: ").split()))
        print("Введите Y через пробел:")
        y = list(map(float, input("Y: ").split()))

    elif choice == "2":
        fname = input("Имя файла: ").strip()
        x, y = [], []
        try:
            with open(fname, "r", encoding="utf-8") as f:
                for line in f:
                    line = line.strip()
                    if not line or line.startswith("#"): continue
                    a, b = line.replace(",", ".").split()
                    x.append(float(a))
                    y.append(float(b))
        except:
            print("Ошибка чтения файла. Используем данные варианта 8.")
            x = [1.10, 1.25, 1.40, 1.55, 1.70, 1.85, 2.00]
            y = [0.2234, 1.2438, 2.2644, 3.2984, 4.3222, 5.3516, 6.3867]

    elif choice == "3":
        print("Функции: 1—sin(x), 2—exp(x), 3—x²")
        f = input("Номер функции: ").strip()
        a = float(input("Начало интервала: "))
        b = float(input("Конец интервала: "))
        n = int(input("Количество точек (≥2): "))
        x = [a + i * (b - a) / (n - 1) for i in range(n)]
        if f == "1":
            y = [math.sin(xi) for xi in x]
        elif f == "2":
            y = [math.exp(xi) for xi in x]
        else:
            y = [xi ** 2 for xi in x]
    else:
        x = [1.10, 1.25, 1.40, 1.55, 1.70, 1.85, 2.00]
        y = [0.2234, 1.2438, 2.2644, 3.2984, 4.3222, 5.3516, 6.3867]

    return x, y


def build_diff_table(y):
    table = [y[:]]
    curr = y[:]
    while len(curr) > 1:
        curr = [curr[i + 1] - curr[i] for i in range(len(curr) - 1)]
        table.append(curr[:])
    return table


def print_diff_table(x, y, table):
    print("\nТаблица конечных разностей:")
    print(f"{'i':>3} {'x':>8} {'y':>10}", end="")
    for k in range(1, len(table)):
        print(f" Δ^{k}y", end="")
    print()
    print("-" * 50)
    for i in range(len(x)):
        print(f"{i:3} {x[i]:8.3f} {y[i]:10.4f}", end="")
        for k in range(1, len(table)):
            if i < len(table[k]):
                print(f" {table[k][i]:8.4f}", end="")
            else:
                print(" " * 9, end="")
        print()


def lagrange(x, y, xv):
    n = len(x)
    result = 0
    for i in range(n):
        basis = 1
        for j in range(n):
            if i != j:
                basis *= (xv - x[j]) / (x[i] - x[j])
        result += y[i] * basis
    return result


def newton_finite(x, y, xv, h):
    table = build_diff_table(y)
    n = len(x) - 1
    mid = (x[0] + x[-1]) / 2

    if xv <= mid:
        t = (xv - x[0]) / h
        res = table[0][0]
        coeff = 1.0
        for k in range(1, n + 1):
            coeff *= (t - k + 1) / k
            res += coeff * table[k][0]
    else:
        t = (xv - x[-1]) / h
        res = table[0][-1]
        coeff = 1.0
        for k in range(1, n + 1):
            coeff *= (t + k - 1) / k
            res += coeff * table[k][-1]
    return res


def gauss(x, y, xv, h):
    table = build_diff_table(y)
    center = len(x) // 2
    t = (xv - x[center]) / h
    res = y[center]

    max_k = min(6, len(table) - 1)

    if xv >= x[center]:
        for k in range(1, max_k + 1):
            if k == 1:
                coeff = t
                idx = center
            elif k == 2:
                coeff = t * (t - 1) / 2
                idx = center - 1
            elif k == 3:
                coeff = (t + 1) * t * (t - 1) / 6
                idx = center - 1
            elif k == 4:
                coeff = (t + 1) * t * (t - 1) * (t - 2) / 24
                idx = center - 2
            elif k == 5:
                coeff = (t + 2) * (t + 1) * t * (t - 1) * (t - 2) / 120
                idx = center - 2
            else:
                coeff = (t + 2) * (t + 1) * t * (t - 1) * (t - 2) * (t - 3) / 720
                idx = center - 3

            if 0 <= idx < len(table[k]):
                res += coeff * table[k][idx]

    else:
        for k in range(1, max_k + 1):
            if k == 1:
                coeff = t
                idx = center - 1
            elif k == 2:
                coeff = t * (t + 1) / 2
                idx = center - 1
            elif k == 3:
                coeff = (t + 1) * t * (t - 1) / 6
                idx = center - 2
            elif k == 4:
                coeff = (t + 2) * (t + 1) * t * (t - 1) / 24
                idx = center - 2
            elif k == 5:
                coeff = (t + 2) * (t + 1) * t * (t - 1) * (t - 2) / 120
                idx = center - 3
            else:
                coeff = (t + 3) * (t + 2) * (t + 1) * t * (t - 1) * (t - 2) / 720
                idx = center - 3

            if 0 <= idx < len(table[k]):
                res += coeff * table[k][idx]

    return res


def stirling(x, y, xv, h):
    table = build_diff_table(y)
    center = len(x) // 2
    t = (xv - x[center]) / h
    res = y[center]
    max_k = len(table) - 1

    for k in range(1, max_k + 1):
        if k % 2 == 1:
            idx1 = center - (k - 1) // 2
            idx2 = center - (k + 1) // 2
            if 0 <= idx2 and idx1 < len(table[k]):
                diff = (table[k][idx1] + table[k][idx2]) / 2
            else:
                break
            coeff = t
            for m in range(1, k // 2 + 1):
                coeff *= (t ** 2 - m ** 2)
            coeff /= math.factorial(k)
        else:
            idx = center - k // 2
            if 0 <= idx < len(table[k]):
                diff = table[k][idx]
            else:
                break
            coeff = t ** 2
            for m in range(1, k // 2):
                coeff *= (t ** 2 - m ** 2)
            coeff /= math.factorial(k)
        res += coeff * diff
    return res


def bessel(x, y, xv, h):
    table = build_diff_table(y)
    center = len(x) // 2
    t = (xv - x[center]) / h

    res = (y[center] + y[center + 1]) / 2 if center + 1 < len(y) else y[center]


    if 0 <= center < len(table[1]):
        res += (t - 0.5) * table[1][center]


    if 0 <= center - 1 and center < len(table[2]):
        coeff = t * (t - 1) / 2
        diff = (table[2][center - 1] + table[2][center]) / 2
        res += coeff * diff


    if 0 <= center - 1 < len(table[3]):
        coeff = (t - 0.5) * t * (t - 1) / 6
        res += coeff * table[3][center - 1]


    if 0 <= center - 2 and center - 1 < len(table[4]):
        coeff = t * (t - 1) * (t + 1) * (t - 2) / 24
        diff = (table[4][center - 2] + table[4][center - 1]) / 2
        res += coeff * diff

    if 0 <= center - 2 < len(table[5]):
        coeff = (t - 0.5) * t * (t - 1) * (t + 1) * (t - 2) / 120
        res += coeff * table[5][center - 2]

    if 0 <= center - 3 and center - 2 < len(table[6]):
        coeff = t * (t - 1) * (t + 1) * (t - 2) * (t + 2) * (t - 3) / 720
        diff = (table[6][center - 3] + table[6][center - 2]) / 2
        res += coeff * diff

    return res

def plot_results(x, y, xv, results, func_true=None):
    x_plot = [x[0] + i * (x[-1] - x[0]) / 200 for i in range(201)]
    y_lag = [lagrange(x, y, xp) for xp in x_plot]
    y_new = [newton_finite(x, y, xp, x[1] - x[0]) for xp in x_plot]
    y_gau = [gauss(x, y, xp, x[1] - x[0]) for xp in x_plot]

    plt.figure(figsize=(10, 6))

    if func_true is not None:
        plt.plot(x_plot, func_true(x_plot), 'k--', label='Истинная функция', linewidth=1, alpha=0.7)

    plt.plot(x_plot, y_lag, 'b-', label='Лагранж', linewidth=1.5)
    plt.plot(x_plot, y_new, 'g--', label='Ньютон', linewidth=1.5)
    plt.plot(x_plot, y_gau, 'r:', label='Гаусс', linewidth=1.5)


    plt.scatter(x, y, c='red', s=80, zorder=5, label='Узлы', edgecolors='black')


    plt.scatter(xv, results['lagrange'], c='yellow', s=120, zorder=6,
                label=f'Точка интерполяции ({xv})', edgecolors='black', marker='*')

    plt.xlabel('x', fontsize=12)
    plt.ylabel('y', fontsize=12)
    plt.title('Интерполяция функции (Вариант 8)', fontsize=14)
    plt.grid(True, alpha=0.3)
    plt.legend(fontsize=10)
    plt.tight_layout()
    plt.show()


def main():
    print("ЛР №5: Интерполяция (Вариант 8)")

    try:
        x, y = read_data()
        h = x[1] - x[0]  # шаг сетки

        print_diff_table(x, y, build_diff_table(y))

        xv = float(input("\nВведите точку для интерполяции: "))

        res_l = lagrange(x, y, xv)
        print(f"\nЛагранж: {res_l:.6f}")

        res_n = newton_finite(x, y, xv, h)
        print(f"Ньютон : {res_n:.6f}")

        res_g = gauss(x, y, xv, h)
        print(f"Гаусс  : {res_g:.6f}")

        res_s = stirling(x, y, xv, h)
        print(f"Стирлинг: {res_s:.6f}")

        res_b = bessel(x, y, xv, h)
        print(f"Бессель : {res_b:.6f}")

        results = {"lagrange": res_l, "newton": res_n, "gauss": res_g}
        plot_results(x, y, xv, results)

    except KeyboardInterrupt:
        print("\nЗавершено.")
    except Exception as e:
        print(f"Ошибка: {e}")
        print("Проверьте ввод данных и повторите.")


if __name__ == "__main__":
    main()
