FROM openjdk:17-jdk-slim
ARG JAR_FILE=/target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} demo-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar", "demo-0.0.1-SNAPSHOT.jar"]