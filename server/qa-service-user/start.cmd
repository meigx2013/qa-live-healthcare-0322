@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 获取脚本所在目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

REM 定义变量
set APP_NAME=qa-service-user
set JAR_FILE=target\%APP_NAME%-0.0.1-SNAPSHOT.jar
set PID_FILE=%APP_NAME%.pid
set LOG_DIR=logs

REM 检查应用是否已经在运行
if exist "%PID_FILE%" (
    set /p PID=<"%PID_FILE%"
    
    REM 检查进程是否存在
    tasklist /FI "PID eq !PID!" 2>nul | find "java" >nul
    if !errorlevel! equ 0 (
        echo 应用已经在运行中，PID: !PID!
        exit /b 1
    ) else (
        echo 发现旧的PID文件，但进程不存在，删除PID文件
        del /f /q "%PID_FILE%" >nul 2>&1
    )
)

REM 检查jar文件是否存在
if not exist "%JAR_FILE%" (
    echo 错误: 找不到jar文件 %JAR_FILE%
    echo 请先执行 mvn clean package 构建项目
    exit /b 1
)

REM 创建日志目录
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM 启动应用
echo 正在启动应用...

REM 使用PowerShell启动进程并获取PID
set PS_CMD=$p = Start-Process -FilePath 'java' -ArgumentList '-jar', '%JAR_FILE%' -WorkingDirectory '%CD%' -RedirectStandardOutput '%LOG_DIR%\application.log' -RedirectStandardError '%LOG_DIR%\error.log' -PassThru; Write-Output $p.Id; $p.Id ^| Out-File -FilePath '%PID_FILE%' -Encoding ascii

for /f "tokens=*" %%i in ('powershell -NoProfile -ExecutionPolicy Bypass -Command "!PS_CMD!"') do set PID=%%i

if "!PID!"=="" (
    echo 错误: 启动应用失败，无法获取PID
    exit /b 1
)

echo 应用已启动，PID: !PID!
echo 日志文件: %LOG_DIR%\application.log

REM 等待几秒确认进程正常运行
timeout /t 3 /nobreak >nul
tasklist /FI "PID eq !PID!" 2>nul | find "java" >nul
if !errorlevel! neq 0 (
    echo 警告: 进程可能启动失败，请检查日志文件
    if exist "%LOG_DIR%\application.log" type "%LOG_DIR%\application.log"
    del /f /q "%PID_FILE%" >nul 2>&1
    exit /b 1
)

echo 启动成功
