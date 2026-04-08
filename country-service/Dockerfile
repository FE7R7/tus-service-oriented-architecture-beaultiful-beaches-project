# Previous Dockerfile setup (without multi-stage builds):

# FROM eclipse-temurin:17-jre

# WORKDIR /app

# Copy the Spring Boot executable jar (repackaged without .original suffix)
# COPY target/country-service-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# -------------------------------------------------------------------------- #

# Multi-Stage Builds setup:
	
# A multi-stage Dockerfile acts as two containers in one sequence.

# The first stage boots up a Maven container, copies the source code inside, and runs the mvn clean package command internally.
# The second stage boots up a tiny Java runner container, grabs the pre-compiled .jar file from Stage 1,
# throws away all the heavy Maven compilation tools and launches the app.
# The "Multi-Stage" build only happens during the creation phase (when run --build).
# The "Maven Builder" (Stage 1) acts like a temporary ghost container. It quickly spins up in the background, compiles the code into a .jar file, 
# hands that .jar file over to Stage 2, and then immediately destroys itself and vanishes from hard drive.
	
# It is not necessary to manually typing (mvn clean package) again.
# So we can simply run in terminal (docker-compose up --build).


# ==========================================
# STAGE 1: Build the application using Maven
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Step 1: Copy over the code and pom.xml
COPY pom.xml .
COPY src ./src

# Step 2: Compile the application directly INSIDE Docker
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Run the compiled Java application
# ==========================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Step 3: Copy only the compiled .jar file from STAGE 1 into STAGE 2
COPY --from=builder /app/target/country-service-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port and boot the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

