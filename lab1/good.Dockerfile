#1: Конкретная версия Python вместо 'latest'
FROM python:3.11-slim

WORKDIR myproject

#2: Копируем только requirements.txt ПЕРЕД установкой
COPY requirements.txt .

# Установка зависимостей (теперь отдельно от кода)
RUN pip install -r requirements.txt

# Копируем код ПОСЛЕ установки зависимостей
COPY . .

#3: Создание непривилегированного пользователя
RUN useradd -m appuser

# Переключение на созданного пользователя для запуска приложения
USER appuser

CMD ["python", "app.py"]