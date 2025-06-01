FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
ARG JAR_FILE=/target/Config-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} Config-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "Config-server-0.0.1-SNAPSHOT.jar"]
