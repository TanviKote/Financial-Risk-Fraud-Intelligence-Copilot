FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY app/pom.xml ./pom.xml
COPY app/src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/financial-risk-fraud-copilot-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
