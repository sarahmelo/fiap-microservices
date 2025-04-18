FROM eclipse-temurin:12-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=out/artifacts/users_jar/users.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]