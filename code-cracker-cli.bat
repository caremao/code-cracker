@echo off
setlocal

:: Ruta relativa al JAR
set JAR_PATH=E:\mao\workspace\code-cracker\cli\target\code-cracker-cli-0.0.1-SNAPSHOT.jar

:: Validar que el JAR existe
if not exist "%JAR_PATH%" (
    echo JAR not found at %JAR_PATH%
    echo Did you run "mvn clean package"?
    pause
    exit /b 1
)

:: Ejecutar el JAR
java -jar %JAR_PATH%

endlocal
