#!/usr/bin/env sh

set -e

APP_HOME=$(cd "$(dirname "$0")" && pwd)
GRADLE_WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

if [ -n "$JAVA_HOME" ]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
else
    JAVA_CMD="java"
fi

if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    echo "Aviso: gradle-wrapper.jar no encontrado en $GRADLE_WRAPPER_JAR."
    if command -v gradle >/dev/null 2>&1; then
        echo "Usando Gradle del sistema como alternativa."
        exec gradle "$@"
    else
        echo "Gradle no está instalado y no se encontró el wrapper."
        exit 1
    fi
fi

exec "$JAVA_CMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
    -classpath "$GRADLE_WRAPPER_JAR" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
