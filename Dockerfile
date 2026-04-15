FROM eclipse-temurin:21-jre-alpine
LABEL authors="egorm"

WORKDIR /app
COPY target/web-service-0.0.1-SNAPSHOT.jar /app/web.jar
EXPOSE 3035
ENTRYPOINT ["java", "-jar", "web.jar"]