@echo off
REM Arranca CumbiaBall en Windows. Uso: scripts\run.bat
setlocal enabledelayedexpansion
cd /d "%~dp0.."

set "JAVA_BIN=java"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"

"%JAVA_BIN%" -version >nul 2>&1
if errorlevel 1 (
  echo.
  echo [ERROR] No encontre ningun JDK. Instala Temurin 17 ^(https://adoptium.net^) y vuelve a intentarlo.
  echo.
  exit /b 1
)

for /f "tokens=3" %%g in ('"%JAVA_BIN%" -version 2^>^&1 ^| findstr /i "version"') do set "JVER=%%g"
set "JVER=%JVER:"=%"
for /f "delims=." %%a in ("%JVER%") do set "MAJOR=%%a"
if "%MAJOR%"=="1" for /f "tokens=2 delims=." %%a in ("%JVER%") do set "MAJOR=%%a"

if %MAJOR% LSS 17 goto :bad_version
if %MAJOR% GTR 25 goto :bad_version

if not "%MAJOR%"=="17" echo   [AVISO] Java %JVER% detectado. La combinacion probada es Temurin 17.

echo   Iniciando CumbiaBall con Java %JVER%...
call gradlew.bat desktop:run
exit /b %errorlevel%

:bad_version
echo.
echo [ERROR] Java %JVER% detectado ^(version mayor %MAJOR%^).
echo   Este proyecto necesita JDK 17 ^(Temurin recomendado^); el rango soportado es 17-25.
echo   Gradle 9 no funciona con versiones anteriores a Java 17.
echo   Revisa la seccion "Instalacion" del README.
echo.
exit /b 1
