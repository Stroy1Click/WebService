FROM openjdk:21
LABEL authors="egorm"

WORKDIR /app
ADD maven/Stroy1Click-WebService-0.0.1-SNAPSHOT.jar /app/web.jar
EXPOSE 3035
ENTRYPOINT ["java", "-jar", "web.jar"]