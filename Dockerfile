# 1. Этап сборки (Build Stage)
FROM eclipse-temurin:23-jdk AS builder

# Устанавливаем Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Копируем pom.xml для кэширования зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src/main/java ./src/main/java
COPY src/main/resources ./src/main/resources

# Собираем JAR
RUN mvn package -DskipTests

# 2. Этап запуска (Runtime Stage)
FROM eclipse-temurin:23-jre
WORKDIR /app

# Копируем JAR из builder-стадии
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]