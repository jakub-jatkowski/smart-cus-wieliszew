@echo off
chcp 65001 >nul
title Smart CUS Wieliszew - Hub Usług Społecznych
cls
echo ==================================================================
echo   🏛️  Smart CUS Wieliszew - Hub Usług Społecznych Gminy Wieliszew
echo ==================================================================
echo.
echo [1/2] Sprawdzanie środowiska Python...
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [BLAD] Nie znaleziono Pythona w systemie!
    echo Zainstaluj Python z https://www.python.org lub uruchom aplikacje
    echo otwierajac plik app_preview\index.html bezposrednio w przegladarce.
    pause
    exit /b 1
)

echo [2/2] Uruchamianie serwera i otwieranie aplikacji w przegladarce...
echo.
python "%~dp0start_preview.py"
pause
