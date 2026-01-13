#!/bin/bash

echo "Starting build process..."

# Install Java 11 if not present
if ! command -v java &> /dev/null; then
    echo "Installing OpenJDK 11..."
    apt-get update
    apt-get install -y openjdk-11-jdk ant
fi

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Install Ant if not present
if ! command -v ant &> /dev/null; then
    echo "Installing Apache Ant..."
    apt-get install -y ant
fi

# Clean and build project
echo "Building project with Ant..."
ant clean
ant compile
ant dist

# Create deployment directory
mkdir -p /opt/tomcat/webapps
cp -r build/web/* /opt/tomcat/webapps/ROOT/

# Copy libraries
mkdir -p /opt/tomcat/lib
cp web/WEB-INF/lib/*.jar /opt/tomcat/lib/ 2>/dev/null || true

# Install Tomcat if not present
if [ ! -d "/opt/tomcat" ]; then
    echo "Installing Apache Tomcat..."
    wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.15/bin/apache-tomcat-10.1.15.tar.gz
    tar -xzf apache-tomcat-10.1.15.tar.gz
    mv apache-tomcat-10.1.15 /opt/tomcat
    chmod +x /opt/tomcat/bin/*.sh
fi

# Update database configuration for production
if [ ! -z "$DATABASE_URL" ]; then
    echo "Updating database configuration..."
    # Create production config
    cat > src/java/config.properties << EOF
# Production Configuration
app.name=Fish Shop
app.version=1.0
app.debug=false

# Database Configuration
db.host=${DB_HOST}
db.port=${DB_PORT}
db.name=${DB_NAME}
db.user=${DB_USER}
db.password=${DB_PASSWORD}
db.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}

# Google OAuth Configuration
google.oauth.client.id=${GOOGLE_OAUTH_CLIENT_ID}
google.oauth.client.secret=${GOOGLE_OAUTH_CLIENT_SECRET}
google.oauth.redirect.uri=${GOOGLE_OAUTH_REDIRECT_URI}

# TinyMCE Configuration
tinymce.api.key=${TINYMCE_API_KEY}
tinymce.version=${TINYMCE_VERSION}

# Blog Settings
blog.posts.per.page=${BLOG_POSTS_PER_PAGE}
blog.featured.posts.count=${BLOG_FEATURED_POSTS_COUNT}
blog.excerpt.length=${BLOG_EXCERPT_LENGTH}

# File Upload Settings
upload.max.size=${UPLOAD_MAX_SIZE}
upload.allowed.types=${UPLOAD_ALLOWED_TYPES}
EOF
fi

echo "Build completed successfully!"
