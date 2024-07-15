# Use a base image that has JDK 11 installed
FROM openjdk:17


# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the containers
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
