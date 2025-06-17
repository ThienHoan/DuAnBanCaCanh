#!/bin/bash

echo "Starting Tomcat server..."

# Set environment variables
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export CATALINA_HOME=/opt/tomcat
export CATALINA_BASE=/opt/tomcat

# Set Tomcat port from environment variable or default to 8080
export PORT=${PORT:-8080}

# Update server.xml with the correct port
sed -i "s/port=\"8080\"/port=\"$PORT\"/g" /opt/tomcat/conf/server.xml

# Create logs directory
mkdir -p /opt/tomcat/logs

# Copy application files
echo "Deploying application..."
rm -rf /opt/tomcat/webapps/ROOT/*
cp -r build/web/* /opt/tomcat/webapps/ROOT/

# Set proper permissions
chmod -R 755 /opt/tomcat/webapps/ROOT/
chmod +x /opt/tomcat/bin/*.sh

# Start Tomcat
echo "Starting Tomcat on port $PORT..."
exec /opt/tomcat/bin/catalina.sh run
