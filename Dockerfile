FROM eclipse-temurin:21-jre-alpine
LABEL authors="egorm"

WORKDIR /app
COPY target/Stroy1Click-WebService-0.0.1-SNAPSHOT.jar /app/web.jar
EXPOSE 3035
ENTRYPOINT ["java", "-jar", "web.jar"]