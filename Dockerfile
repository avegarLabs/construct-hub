# ============================================
# Stage 1: Build Application
# ============================================
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Install required tools for health check
RUN apk add --no-cache wget

# Copy Maven wrapper and pom.xml first for better caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests in Docker build)
RUN ./mvnw package -DskipTests -B

# ============================================
# Stage 2: Runtime Image
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/construct-hub-0.0.1.jar app.jar

# Change ownership to non-root user
RUN chown -R spring:spring /app

# Create logs directory
RUN mkdir -p /app/logs && chown -R spring:spring /app/logs

# Switch to non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Environment variables (can be overridden)
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
