@echo off
title Server Antrian Web
echo ==============================================
echo  Menjalankan Server Web Antrian (Port 8080)
echo ==============================================
echo.
echo Meng-compile kode Java...
cd src
javac -cp ".;..\lib\mysql-connector-j-8.3.0.jar" *.java
if %errorlevel% neq 0 (
    echo Gagal meng-compile. Periksa error di atas.
    pause
    exit /b %errorlevel%
)
echo Berhasil di-compile!
echo.
echo Menjalankan Server...
java -cp ".;..\lib\mysql-connector-j-8.3.0.jar" Main
pause
