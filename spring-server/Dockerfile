FROM openjdk:17
WORKDIR /
ADD target/distanceinair-server-1.0.0.jar app.jar
RUN useradd -m rootless
USER rootless
EXPOSE 8082
CMD java -jar app.jar