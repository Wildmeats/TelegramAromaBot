FROM openjdk:17-jdk
WORKDIR / app
COPY . /app
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/AromaRootsBot.jar"]