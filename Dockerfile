FROM openjdk:8-jdk-alpine
MAINTAINER sk
COPY /build/libs/cassandra-udt-springboot2-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

