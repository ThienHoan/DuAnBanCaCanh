# Download Kotlin libraries for OkHttp compatibility
$webInfLib = "web\WEB-INF\lib"

# Create web\WEB-INF\lib directory if it doesn't exist
if (!(Test-Path $webInfLib)) {
    New-Item -ItemType Directory -Path $webInfLib -Force
}

# Download Kotlin Standard Library
$kotlinStdlibUrl = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib/1.9.10/kotlin-stdlib-1.9.10.jar"
$kotlinStdlibCommonUrl = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar"
$kotlinStdlibJdk8Url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.9.10/kotlin-stdlib-jdk8-1.9.10.jar"

$kotlinStdlibPath = "$webInfLib\kotlin-stdlib-1.9.10.jar"
$kotlinStdlibCommonPath = "$webInfLib\kotlin-stdlib-common-1.9.10.jar"
$kotlinStdlibJdk8Path = "$webInfLib\kotlin-stdlib-jdk8-1.9.10.jar"

Write-Host "Downloading Kotlin Standard Library..."
try {
    Invoke-WebRequest -Uri $kotlinStdlibUrl -OutFile $kotlinStdlibPath
    Write-Host "✓ Downloaded kotlin-stdlib-1.9.10.jar"
} catch {
    Write-Host "✗ Failed to download kotlin-stdlib: $($_.Exception.Message)"
}

Write-Host "Downloading Kotlin Standard Library Common..."
try {
    Invoke-WebRequest -Uri $kotlinStdlibCommonUrl -OutFile $kotlinStdlibCommonPath
    Write-Host "✓ Downloaded kotlin-stdlib-common-1.9.10.jar"
} catch {
    Write-Host "✗ Failed to download kotlin-stdlib-common: $($_.Exception.Message)"
}

Write-Host "Downloading Kotlin Standard Library JDK8..."
try {
    Invoke-WebRequest -Uri $kotlinStdlibJdk8Url -OutFile $kotlinStdlibJdk8Path
    Write-Host "✓ Downloaded kotlin-stdlib-jdk8-1.9.10.jar"
} catch {
    Write-Host "✗ Failed to download kotlin-stdlib-jdk8: $($_.Exception.Message)"
}

Write-Host "`nChecking downloaded files..."
if (Test-Path $kotlinStdlibPath) {
    $size = (Get-Item $kotlinStdlibPath).Length
    Write-Host "✓ kotlin-stdlib-1.9.10.jar ($($size) bytes)"
} else {
    Write-Host "✗ kotlin-stdlib-1.9.10.jar not found"
}

if (Test-Path $kotlinStdlibCommonPath) {
    $size = (Get-Item $kotlinStdlibCommonPath).Length
    Write-Host "✓ kotlin-stdlib-common-1.9.10.jar ($($size) bytes)"
} else {
    Write-Host "✗ kotlin-stdlib-common-1.9.10.jar not found"
}

if (Test-Path $kotlinStdlibJdk8Path) {
    $size = (Get-Item $kotlinStdlibJdk8Path).Length
    Write-Host "✓ kotlin-stdlib-jdk8-1.9.10.jar ($($size) bytes)"
} else {
    Write-Host "✗ kotlin-stdlib-jdk8-1.9.10.jar not found"
}

Write-Host "`nDone! Kotlin libraries have been downloaded to web\WEB-INF\lib\"
