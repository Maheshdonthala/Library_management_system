# Multi-stage Dockerfile: build with Maven, run with Eclipse Temurin JRE

# Build stage
FROM maven:3.8.8-eclipse-temurin-17 AS build
# Use the Maven image's installed mvn. Do not rely on the repo containing the Maven wrapper.
WORKDIR /workspace
# Copy only the POM and source tree. The builder image already has Maven installed.
COPY pom.xml ./
# copy sources
COPY src ./src
# package
RUN mvn -B -DskipTests package

# Run stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/target/*.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m -Dcom.mongodb.dns.resolver=dnsjava"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
