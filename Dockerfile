FROM openjdk:11.0.5-jdk
ADD build/libs/*.jar /opt/helloworld/

EXPOSE 8080

CMD ["java", "-jar", "/opt/helloworld/helloworld-1.0-SNAPSHOT.jar"]
