
import pandas as pd
import matplotlib.pyplot as plt

# Чтение данных из CSV файла
data = pd.read_csv("dop5.csv", delimiter=';')

# Преобразование столбца 'дата' в формат datetime
data['дата'] = pd.to_datetime(data['дата'], format='%d/%m/%y')

# Настройка размера графика
plt.figure(figsize=(12, 6))

# Создание списка для данных боксплота
data_to_plot = []

# Группировка данных по дате и добавление значений в data_to_plot
for date in data['дата'].unique():
    daily_data = data[data['дата'] == date]
    data_to_plot.append([
        daily_data['открытие'].values,
        daily_data['закрытие'].values,
        daily_data['мин'].values,
        daily_data['макс'].values
    ])

# Преобразуем список в формат, удобный для boxplot
data_to_plot = [item for sublist in data_to_plot for item in sublist]

# Построение диаграммы усов
plt.boxplot(data_to_plot, labels=[f"{date.strftime('%d/%m/%y')} ({param})" for date in data['дата'].unique() for param in ['открытие', 'закрытие', 'мин', 'макс']])

# Настройка меток и заголовка
plt.ylabel('Цена')
plt.xlabel('Дата и Параметр')
plt.title('Диаграмма усов по датам и параметрам')
plt.xticks(rotation=45)
plt.grid()

# Отображение графика
plt.tight_layout()  # Уплотнение графика для лучшего отображения
plt.show()