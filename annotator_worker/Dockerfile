FROM gradle:jdk17-alpine
WORKDIR /app
COPY . .
RUN gradle build
CMD ["java", "-jar", "build/libs/annotator_worker-0.0.1-SNAPSHOT.jar"]