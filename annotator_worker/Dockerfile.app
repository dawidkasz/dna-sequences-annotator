FROM percival33/pzsp2-worker:1.0.0
WORKDIR /app/annotator_worker

# Copy the Java application source
COPY . .

# Build the Java application using Gradle
RUN ./gradlew build

# Start service
CMD ["java", "-jar", "build/libs/annotator_worker-0.0.1-SNAPSHOT.jar"]