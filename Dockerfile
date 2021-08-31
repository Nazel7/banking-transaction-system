FROM openjdk:14-jdk-alpine
ADD pom.xml ./
ADD target/bank.0.0.1-SNAPSHOT bank.0.0.1-SNAPSHOT
ENTRYPOINT ["java", "-jar", "bank 0.0.1-SNAPSHOT"]
VOLUME /dev/student
EXPOSE 8000
