# Start with a Java 17 runtime. Production only needs the JRE, not the JDK.
FROM eclipse-temurin:17-jre

# Choose where the app lives inside the container.
WORKDIR /app

# Copy the tested, versioned JAR the factory produced.
COPY target/app.jar app.jar

EXPOSE 8081

# Define how the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
