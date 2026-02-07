@echo off
setlocal

set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

if defined JAVA_HOME (
  set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
  set JAVA_EXE=java.exe
)

if not exist "%WRAPPER_JAR%" (
  echo Aviso: gradle-wrapper.jar no encontrado en %WRAPPER_JAR%.
  where gradle >NUL 2>&1
  if "%ERRORLEVEL%" == "0" (
    echo Usando Gradle del sistema como alternativa.
    gradle %*
    exit /b %ERRORLEVEL%
  ) else (
    echo Gradle no esta instalado y no se encontro el wrapper.
    exit /b 1
  )
)

"%JAVA_EXE%" -Xmx64m -Xms64m %JAVA_OPTS% %GRADLE_OPTS% -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
