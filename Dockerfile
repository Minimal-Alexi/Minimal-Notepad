FROM openjdk:17
WORKDIR /app
COPY target/*.jar app.jar
COPY .env /app/.env
EXPOSE 8093
ENTRYPOINT ["java", "-jar", "app.jar"]