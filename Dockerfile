

FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
COPY src/main/resources/serviceAccountKey.json ./serviceAccountKey.json
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
COPY --from=builder /app/serviceAccountKey.json ./serviceAccountKey.json
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
x