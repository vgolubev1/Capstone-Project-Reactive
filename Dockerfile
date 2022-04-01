FROM openjdk:11
ADD target/main-service-0.0.1-SNAPSHOT.jar main-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "main-service-0.0.1-SNAPSHOT.jar"]