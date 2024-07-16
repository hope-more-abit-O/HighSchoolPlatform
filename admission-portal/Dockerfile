# Use a base image that has JDK 11 installed
FROM openjdk:17


# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the containers
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]

# Set enviromentvariable
ENV SECRET_KEY="94b83cf0b942b22cc7c98924f90eec3bc90b974d61392b4eb694b85f7c68265e"
ENV EXPIRATION="86400000"