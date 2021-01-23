FROM openjdk:15-alpine
EXPOSE 3333

RUN mkdir /app
WORKDIR /app
COPY ./build/libs/*.jar /app/DistroDB.jar

ENTRYPOINT java -jar /app/DistroDB.jar