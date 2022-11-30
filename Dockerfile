FROM openjdk:11
ADD target/todo-list.jar todo-list.jar
ENTRYPOINT ["java","-jar","todo-list.jar"]