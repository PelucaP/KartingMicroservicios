FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/*.jar reservacomprobante-service.jar
ENTRYPOINT ["java", "-jar", "reservacomprobante-service.jar"]