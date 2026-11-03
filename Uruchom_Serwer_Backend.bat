@echo off
chcp 65001 >nul
title Smart CUS Wieliszew - Backend API Server
echo ========================================================
echo  🏛️ Smart CUS Wieliszew - Uruchamianie Serwera Backend
echo  Port: 8000 (wszystkie interfejsy: 0.0.0.0)
echo  Dokumentacja Swagger: http://localhost:8000/docs
echo ========================================================
set "PYTHONIOENCODING=utf-8"
cd /d "%~dp0backend"
set "PYTHONPATH=%~dp0backend;%~dp0"
python run_server.py
if errorlevel 1 (
    echo.
    echo Wystapil blad podczas uruchamiania serwera.
    pause
)

