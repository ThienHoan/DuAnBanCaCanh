FROM tomcat:10.1.24-jdk17-temurin
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
