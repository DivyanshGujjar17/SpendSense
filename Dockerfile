# ---- Build Stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy everything into the container
COPY . .

# Build the Spring Boot jar using Maven Wrapper (mvnw)
RUN ./mvnw clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/SpendSense-0.0.1-SNAPSHOT.jar SpendSense-v1.0.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "SpendSense-v1.0.jar"]
