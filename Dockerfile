FROM openjdk:17
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY build/libs/test-0.0.1-SNAPSHOT.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar