FROM gradle:jdk17-focal

WORKDIR /app

COPY . .

RUN gradle build

EXPOSE 8080