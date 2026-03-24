#1: Использование тега 'latest'
FROM python:latest
WORKDIR myproject

#2: Неправильный порядок COPY и RUN
COPY . .

#3: Отсутствие создания пользователя
RUN pip install -r requirements.txt

CMD ["python", "app.py"]