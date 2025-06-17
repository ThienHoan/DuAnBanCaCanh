FROM openjdk:11-jdk-slim

# Install required packages
RUN apt-get update && apt-get install -y \
    ant \
    wget \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Set environment variables
ENV JAVA_HOME=/usr/local/openjdk-11
ENV CATALINA_HOME=/opt/tomcat
ENV CATALINA_BASE=/opt/tomcat

# Download and install Tomcat
RUN wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.15/bin/apache-tomcat-10.1.15.tar.gz \
    && tar -xzf apache-tomcat-10.1.15.tar.gz \
    && mv apache-tomcat-10.1.15 /opt/tomcat \
    && rm apache-tomcat-10.1.15.tar.gz \
    && chmod +x /opt/tomcat/bin/*.sh

# Make scripts executable
RUN chmod +x ./scripts/*.sh

# Build the application
RUN ./scripts/build.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Start the application
CMD ["./scripts/start.sh"]
