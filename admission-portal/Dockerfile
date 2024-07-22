# Use a base image that has JDK 17 installed
FROM openjdk:17

# Create a directory for the app
RUN mkdir /app

# Set the working directory
WORKDIR /app

# Copy the application's jar file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy the keystore into the container
COPY src/main/resources/keystore.p12 /app/keystore.p12

# Set environment variables for the application
ENV SECRET_KEY="94b83cf0b942b22cc7c98924f90eec3bc90b974d61392b4eb694b85f7c68265e"
ENV EXPIRATION="86400000"
# Expose port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
