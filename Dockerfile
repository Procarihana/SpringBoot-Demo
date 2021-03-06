FROM openjdk:8u265-slim-buster

RUN mkdir /dockerworkdir

WORKDIR /dockerworkdir

COPY  target/spring-boot-1.1.1-SNAPSHOT.jar /dockerworkdir

EXPOSE 8080

CMD ["java","-jar","spring-boot-1.1.1-SNAPSHOT.jar"]


