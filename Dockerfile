# Build env
FROM maven:3.8.5-openjdk-17 AS build-java-stage
WORKDIR /rest-server
COPY serveur-rest serveur-rest
COPY Business Business
COPY client-rest client-rest
COPY pom.xml .
RUN mvn clean package

# Run env
FROM openjdk:17.0.2-jdk-slim-buster
COPY --from=build-java-stage  /rest-server/serveur-rest/target/classes /rest-server/classes
COPY --from=build-java-stage  /rest-server/serveur-rest/target/dependency /rest-server/dependency
EXPOSE 8082
ENTRYPOINT ["java", "-cp", "/rest-server/classes:/rest-server/dependency/*", "com.kumuluz.ee.EeApplication"]
