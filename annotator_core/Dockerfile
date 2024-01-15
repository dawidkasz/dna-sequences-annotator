FROM gradle:jdk17-focal
WORKDIR /app
COPY . .
RUN gradle build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/annotator_core-0.0.1-SNAPSHOT.jar"]
