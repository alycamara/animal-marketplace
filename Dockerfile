FROM adoptopenjdk/openjdk17:alpine-jre
ARG JAR_FILE=target/animalMarketplacePlatformApplication.jar
WORKDIR /opt/app
COPY ${JAR_FILE} animalMarketplacePlatformApplication.jar
COPY entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
