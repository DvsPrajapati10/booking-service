FROM adoptopenjdk/maven-openjdk11

COPY target/booking-service-0.0.1.jar app.jar

EXPOSE 8074:8074

RUN apt-get update
RUN apt-get install -y gcc
RUN apt-get install -y curl

ENTRYPOINT ["java","-jar","app.jar"]