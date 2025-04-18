FROM eclipse-temurin:12-alpine
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=out/artifacts/sprinkler_jar/sprinkler.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]