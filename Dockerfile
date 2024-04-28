FROM openjdk:22-jdk-slim
WORKDIR /brokerapp
ADD target/*.jar brokerapp.jar
ENTRYPOINT ["java","-jar","brokerapp.jar"]