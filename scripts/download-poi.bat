@echo off
setlocal

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib
cd lib

REM Download Apache POI libraries
curl -L -o poi-bin-5.2.3-20220909.zip https://dlcdn.apache.org/poi/release/bin/poi-bin-5.2.3-20220909.zip
tar -xf poi-bin-5.2.3-20220909.zip

REM Copy required JAR files
copy poi-bin-5.2.3\poi-5.2.3.jar .
copy poi-bin-5.2.3\poi-ooxml-5.2.3.jar .
copy poi-bin-5.2.3\poi-ooxml-lite-5.2.3.jar .
copy poi-bin-5.2.3\ooxml-lib\xmlbeans-5.1.1.jar .
copy poi-bin-5.2.3\lib\commons-collections4-4.4.jar .
copy poi-bin-5.2.3\lib\commons-compress-1.21.jar .
copy poi-bin-5.2.3\lib\commons-io-2.11.0.jar .
copy poi-bin-5.2.3\lib\commons-math3-3.6.1.jar .
copy poi-bin-5.2.3\lib\log4j-api-2.18.0.jar .
copy poi-bin-5.2.3\lib\SparseBitSet-1.2.jar .

REM Clean up
rmdir /s /q poi-bin-5.2.3
del poi-bin-5.2.3-20220909.zip

echo POI libraries downloaded successfully!
pause 