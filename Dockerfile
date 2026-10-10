# ЭТАП 1: Сборка проекта (используем образ с Maven)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Копируем файл зависимостей и исходный код
COPY pom.xml .
COPY src ./src
# Собираем .jar файл, пропуская тесты (чтобы было быстрее)
RUN mvn clean package -DskipTests

# ЭТАП 2: Создание финального легкого образа (только JRE для запуска)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Копируем собранный .jar файл из первого этапа
COPY --from=build /app/target/*.jar app.jar

# Говорим, что приложение работает на порту 8081
EXPOSE 8081

# Команда для запуска нашего приложения
ENTRYPOINT ["java", "-jar", "app.jar"]