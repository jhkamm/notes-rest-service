FROM openjdk:11

RUN apt update && apt install -y default-mysql-client

# Copy gradle-built jar into container
COPY ./build/libs/*.jar /app/

EXPOSE 8080
