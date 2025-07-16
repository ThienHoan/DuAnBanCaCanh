#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib
mkdir -p web/WEB-INF/lib

# Download Gson library
echo "Downloading Gson library..."

# Gson
wget -c https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar -P lib/

# Copy all JAR files to WEB-INF/lib
echo "Copying libraries to web/WEB-INF/lib..."
cp lib/gson-2.10.1.jar web/WEB-INF/lib/

echo "Gson library downloaded and installed successfully!" 