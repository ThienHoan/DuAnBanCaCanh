#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib
cd lib

# Download iText PDF library
wget https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13.3/itextpdf-5.5.13.3.jar

# Download Apache Commons CSV
wget https://repo1.maven.org/maven2/org/apache/commons/commons-csv/1.10.0/commons-csv-1.10.0.jar

# Note: POI libraries are already in the project for Excel and Word support

echo "Libraries downloaded successfully!" 