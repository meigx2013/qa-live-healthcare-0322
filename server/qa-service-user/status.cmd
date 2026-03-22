@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 获取脚本所在目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

REM 定义变量
set APP_NAME=qa-service-user
set PID_FILE=%APP_NAME%.pid

REM 检查PID文件是否存在
if not exist "%PID_FILE%" (
    echo 应用未运行 ^(PID文件不存在^)
    exit /b 1
)

REM 读取PID
set /p PID=<"%PID_FILE%"

REM 检查进程是否在运行
tasklist /FI "PID eq %PID%" 2>nul | find "java" >nul
if !errorlevel! equ 0 (
    echo 应用正在运行，PID: %PID%
    echo.
    echo 进程信息:
    tasklist /FI "PID eq %PID%" /V
) else (
    echo 应用未运行 ^(PID文件存在但进程不存在^)
    echo 清理无效的PID文件
    del /f /q "%PID_FILE%" >nul 2>&1
    exit /b 1
)
