# Используем легкий образ с Java
FROM eclipse-temurin:17-jre-alpine
# Создаем папку для приложения в контейнере
WORKDIR /app
# Копируем собранный jar-файл из папки target в контейнер
COPY target/*.jar /app/app.jar
# Команда для запуска бота
ENTRYPOINT ["java", "-jar", "app.jar"]