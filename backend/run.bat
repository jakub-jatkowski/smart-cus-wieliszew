@echo off
title Smart CUS Wieliszew - Backend API Server
echo ========================================================
echo  🏛️ Smart CUS Wieliszew - Uruchamianie Serwera Backend
echo  Port: 8000 (wszystkie interfejsy: 0.0.0.0)
echo  Dokumentacja Swagger: http://localhost:8000/docs
echo ========================================================
cd /d "%~dp0\.."
python backend\run_server.py
pause

