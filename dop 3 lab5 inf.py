import pandas as pd
import matplotlib.pyplot as plt


data = pd.read_csv("data.csv", delimiter=';')
data['дата'] = pd.to_datetime(data['дата'], format='%d/%m/%y')
plt.figure(figsize=(12, 8))
data_to_plot = []
for date in data['дата'].unique():
    daily_data = data[data['дата'] == date]
    data_to_plot.append(daily_data['открытие'].values)
    data_to_plot.append(daily_data['закрытие'].values)
    data_to_plot.append(daily_data['мин'].values)
    data_to_plot.append(daily_data['макс'].values)

plt.boxplot(data_to_plot)
plt.ylabel('Цена')
plt.xlabel('Дата')
plt.title('Диаграмма усов')
unique_dates = data['дата'].unique()
plt.xticks(ticks=range(1, len(data_to_plot) + 1),
           labels=[f"{date.strftime('%d/%m/%y')}" if i % 4 == 0 else "" for i, date in enumerate(unique_dates.repeat(4))])

plt.grid()
plt.tight_layout()
plt.show()


