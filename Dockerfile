# the base image
FROM amazoncorretto:21

WORKDIR /app

# the JAR file path
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the build context into the Docker image
COPY ${JAR_FILE} /app/application.jar

CMD apt-get update -y

# Set the default command to run the Java application
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app/application.jar"]