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
    echo 正在停止应用，PID: %PID%
    
    REM 尝试正常停止
    taskkill /PID %PID% >nul 2>&1
    
    REM 等待进程结束
    set count=0
    :wait_loop
    set /a count+=1
    if !count! gtr 10 goto force_kill
    
    tasklist /FI "PID eq %PID%" 2>nul | find "java" >nul
    if !errorlevel! neq 0 (
        echo 应用已成功停止
        del /f /q "%PID_FILE%" >nul 2>&1
        exit /b 0
    )
    
    echo 等待进程结束... ^(!count!/10^)
    timeout /t 1 /nobreak >nul
    goto wait_loop
    
    :force_kill
    REM 如果进程仍然存在，强制终止
    echo 进程未能正常结束，强制终止...
    taskkill /F /PID %PID% >nul 2>&1
    
    REM 再次检查
    tasklist /FI "PID eq %PID%" 2>nul | find "java" >nul
    if !errorlevel! neq 0 (
        echo 应用已强制停止
        del /f /q "%PID_FILE%" >nul 2>&1
        exit /b 0
    ) else (
        echo 错误: 无法停止进程 %PID%
        exit /b 1
    )
) else (
    echo 应用未运行 ^(PID文件存在但进程不存在^)
    echo 清理无效的PID文件
    del /f /q "%PID_FILE%" >nul 2>&1
    exit /b 1
)
