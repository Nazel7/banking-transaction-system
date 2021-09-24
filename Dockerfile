FROM openjdk:14-jdk-alpine
ADD pom.xml ./
ADD target/bank.0.0.1-SNAPSHOT.jar bank.0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "bank.0.0.1-SNAPSHOT.jar"]
VOLUME /dev/student
EXPOSE 8000
