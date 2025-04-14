FROM amazoncorretto:17-al2023-jdk

RUN mkdir -p /opt/multipass-service
RUN mkdir -p /mnt/data

USER nobody

COPY target/*.jar /opt/multipass-service/
COPY target/*.jar /opt/multipass-service/resources/lib/
ENV MULTIPASS_PATH_FLOWS=/flows
ENV MULTIPASS_PATH_FILES_JAVA=/java
ENV DATA_PATH=/data
ENV RESOURCE_PATH=/resources
EXPOSE 8080
WORKDIR /opt/multipass-service

CMD java -XX:+ExitOnOutOfMemoryError -XX:+UseCompressedOops -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=30 -XX:MaxGCPauseMillis=10000 -XX:MaxRAMPercentage=40.0 -jar multipass-rest-api-demo-1.0-SNAPSHOT.jar
