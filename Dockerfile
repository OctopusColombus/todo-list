FROM openjdk:11-jdk-slim
ADD target/todo-list.jar todo-list.jar
ENTRYPOINT ["java","-jar","todo-list.jar"]