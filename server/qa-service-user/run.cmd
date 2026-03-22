@echo off
cd /d "%~dp0"
echo Starting Spring Boot application...
java -jar target\qa-service-user-0.0.1-SNAPSHOT.jar
