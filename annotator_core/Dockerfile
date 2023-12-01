FROM gradle:jdk17-alpine
WORKDIR /app
COPY . .
RUN gradle build
EXPOSE 8080
CMD ["java", "-jar", "build/libs/annotator_core-0.0.1-SNAPSHOT.jar"]
