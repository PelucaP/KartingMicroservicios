FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/*.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]