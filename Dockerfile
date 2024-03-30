# Двухэтапный докерфайл для разделения этапа сборки и запуска приложения
## Первый этап - Сборка файла
FROM maven:3-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Pprod -DskipTests

## Второй этап - запуск приложения
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Копируем образ приложения с этапа сборки
COPY --from=build /app/target/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]