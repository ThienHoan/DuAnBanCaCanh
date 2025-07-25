#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib
cd lib

# Download Apache POI libraries
wget https://dlcdn.apache.org/poi/release/bin/poi-bin-5.2.3-20220909.zip
unzip poi-bin-5.2.3-20220909.zip

# Copy required JAR files
cp poi-bin-5.2.3/poi-5.2.3.jar .
cp poi-bin-5.2.3/poi-ooxml-5.2.3.jar .
cp poi-bin-5.2.3/poi-ooxml-lite-5.2.3.jar .
cp poi-bin-5.2.3/ooxml-lib/xmlbeans-5.1.1.jar .
cp poi-bin-5.2.3/lib/commons-collections4-4.4.jar .
cp poi-bin-5.2.3/lib/commons-compress-1.21.jar .
cp poi-bin-5.2.3/lib/commons-io-2.11.0.jar .
cp poi-bin-5.2.3/lib/commons-math3-3.6.1.jar .
cp poi-bin-5.2.3/lib/log4j-api-2.18.0.jar .
cp poi-bin-5.2.3/lib/SparseBitSet-1.2.jar .

# Clean up
rm -rf poi-bin-5.2.3
rm poi-bin-5.2.3-20220909.zip

echo "POI libraries downloaded successfully!" 