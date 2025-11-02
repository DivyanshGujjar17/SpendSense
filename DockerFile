From eclipse-temurin:21-jre
WORKDIR /app
COPY target/SpendSense-0.0.1-SNAPSHOT.jar SpendSense-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java" ,"-jar","SpendSense-v1.0.jar"]