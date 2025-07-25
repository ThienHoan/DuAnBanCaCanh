@echo off
setlocal

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib
cd lib

REM Download iText PDF library
curl -O https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13.3/itextpdf-5.5.13.3.jar

REM Download Apache Commons CSV
curl -O https://repo1.maven.org/maven2/org/apache/commons/commons-csv/1.10.0/commons-csv-1.10.0.jar

REM Note: POI libraries are already in the project for Excel and Word support

echo Libraries downloaded successfully!
pause 