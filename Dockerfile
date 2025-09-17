# --- Build Stage ---
FROM eclipse-temurin:17 AS build

WORKDIR /app

# Create a non-root user for building
# Using --system for a system user, which is generally preferred for applications
RUN addgroup --system appgroup && adduser --system --ingroup appuser
USER appuser

# Copy Maven wrapper and pom.xml first to leverage Docker cache for dependencies
COPY --chown=appuser:appgroup pom.xml ./pom.xml
COPY --chown=appuser:appgroup mvnw ./mvnw
COPY --chown=appuser:appgroup .mvn ./.mvn

# Download dependencies to leverage Docker layer caching
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application source code
COPY --chown=appuser:appgroup src ./src

# Package the application, skipping tests for faster build in Docker
RUN ./mvnw clean package -DskipTests

# Determine required modules for jlink from the fat JAR
# This command should analyze the final fat JAR before extraction
RUN export JAR_FILE=$(find target -name "*.jar" | head -n 1) && \
    export MODULES=$(jdeps --ignore-missing-deps -q --recursive --multi-release 17 --print-module-deps $JAR_FILE) && \
    jlink --module-path $JAVA_HOME/jmods --add-modules ${MODULES} --output /opt/jre --compress=2 --no-header-files --no-man-pages

# Extract Spring Boot layers. This creates directories like BOOT-INF/lib, BOOT-INF/classes, etc.
RUN java -Djarmode=tools -jar target/*.jar extract

# --- Runtime Stage ---
FROM alpine:3.18

# Copy the custom JRE from the build stage
COPY --from=build --chown=appuser:appgroup /opt/jre /opt/jre
ENV JAVA_HOME=/opt/jre
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV JAVA_TOOL_OPTIONS=""

# Create the same non-root user for running the application
RUN addgroup --system appgroup && adduser --system --ingroup appuser

WORKDIR /app

# Copy only the extracted Spring Boot layers from the build stage
# This is crucial for smaller image size and better caching
COPY --from=build --chown=appuser:appgroup /app/BOOT-INF/lib /app/BOOT-INF/lib
COPY --from=build --chown=appuser:appgroup /app/BOOT-INF/classes /app/BOOT-INF/classes
COPY --from=build --chown=appuser:appgroup /app/META-INF /app/META-INF
COPY --from=build --chown=appuser:appgroup /app/org /app/org

# Set the user for the runtime container
USER appuser

# Use the Spring Boot JarLauncher to run the application from its extracted layers
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
