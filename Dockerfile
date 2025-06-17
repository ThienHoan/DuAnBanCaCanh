FROM openjdk:11-jdk-slim

# Install required packages
RUN apt-get update && apt-get install -y \
    ant \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Download and install Tomcat
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.15/bin/apache-tomcat-10.1.15.tar.gz \
    && tar -xzf apache-tomcat-10.1.15.tar.gz \
    && mv apache-tomcat-10.1.15 /opt/tomcat \
    && rm apache-tomcat-10.1.15.tar.gz

# Build the application
RUN ant clean compile dist

# Copy built app to Tomcat
RUN cp -r build/web/* /opt/tomcat/webapps/ROOT/

# Set permissions
RUN chmod +x /opt/tomcat/bin/*.sh

# Expose port for Render
EXPOSE $PORT

# Create start script for Render
RUN echo '#!/bin/bash\n\
export CATALINA_HOME=/opt/tomcat\n\
export JAVA_HOME=/usr/local/openjdk-11\n\
export PORT=${PORT:-10000}\n\
echo "Starting Tomcat on port $PORT"\n\
sed -i "s/port=\"8080\"/port=\"$PORT\"/g" /opt/tomcat/conf/server.xml\n\
exec /opt/tomcat/bin/catalina.sh run' > /app/start-render.sh \
    && chmod +x /app/start-render.sh

# Start the application
CMD ["/app/start-render.sh"]
