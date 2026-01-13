@echo off
echo Downloading Gson library...

REM Create directories if they don't exist
if not exist "lib" mkdir lib
if not exist "web\WEB-INF\lib" mkdir web\WEB-INF\lib

REM Set download URL
set GSON_URL=https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar

REM Download Gson using PowerShell
echo Downloading Gson...
powershell -Command "(New-Object Net.WebClient).DownloadFile('%GSON_URL%', 'lib\gson-2.10.1.jar')"

REM Copy JAR file to WEB-INF/lib
echo Copying library to web\WEB-INF\lib...
copy lib\gson-2.10.1.jar web\WEB-INF\lib\

echo Gson library downloaded and installed successfully!
pause 