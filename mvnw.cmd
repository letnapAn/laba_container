@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
if "%MAVEN_PROJECTBASEDIR:~-1%"=="\" set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%

set WRAPPER_JAR_PATH=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if not exist "%WRAPPER_JAR_PATH%" (
    echo ERROR: Wrapper jar not found at %WRAPPER_JAR_PATH%
    exit /b 1
)

"%JAVA_EXE%" -cp "%WRAPPER_JAR_PATH%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*