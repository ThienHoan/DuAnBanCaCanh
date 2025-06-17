FROM tomcat:10.1.24-jdk17-temurin

# Cài đặt Ant (bản mới nhất)
RUN apt-get update && \
    apt-get install -y ant && \
    rm -rf /var/lib/apt/lists/*

# Copy toàn bộ source code vào container
WORKDIR /app
COPY . .

# Build project bằng Ant (nếu build.xml ở gốc repo)
RUN ant clean && ant

# Copy file .war vào Tomcat (tùy vào cấu hình build.xml của bạn, thường sẽ nằm ở dist/)
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
