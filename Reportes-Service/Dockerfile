FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/*.jar Reportes-Service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Reportes-Service-0.0.1-SNAPSHOT.jar"]