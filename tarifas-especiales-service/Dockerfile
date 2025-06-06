FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/*.jar tarifas-especiales-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "tarifas-especiales-service-0.0.1-SNAPSHOT.jar"]