@echo off
chcp 65001 >nul

REM 获取脚本所在目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

echo 正在重启应用...

REM 停止应用
echo 1. 停止应用...
call stop.cmd

REM 等待一秒确保进程完全结束
timeout /t 1 /nobreak >nul

REM 启动应用
echo 2. 启动应用...
call start.cmd

echo 应用重启完成
