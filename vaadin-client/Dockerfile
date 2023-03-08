FROM openjdk:17
WORKDIR /
ADD target/distanceinair-client-1.0.0.jar app.jar
RUN useradd -m rootless
USER rootless
EXPOSE 8080
CMD java -jar app.jar