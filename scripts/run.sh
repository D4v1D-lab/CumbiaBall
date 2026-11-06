#!/usr/bin/env bash
# Arranca CumbiaBall en Linux, macOS o GitHub Codespaces.
# Uso: ./scripts/run.sh
set -euo pipefail
cd "$(dirname "$0")/.."

info()  { printf '  %s\n' "$*"; }
fail()  { printf '\n❌ %s\n\n' "$*" >&2; exit 1; }

# --- 1. Localizar el JDK -----------------------------------------------------
JAVA_BIN="java"
if [ -n "${JAVA_HOME:-}" ] && [ -x "${JAVA_HOME}/bin/java" ]; then
  JAVA_BIN="${JAVA_HOME}/bin/java"
fi

if ! command -v "$JAVA_BIN" >/dev/null 2>&1 && [ ! -x "$JAVA_BIN" ]; then
  fail "No encontré ningún JDK. Instala Temurin 11 (https://adoptium.net) y vuelve a intentarlo."
fi

version_line="$("$JAVA_BIN" -version 2>&1 | awk -F'"' '/version/ {print $2; exit}')"
if [ -z "$version_line" ]; then
  fail "No pude leer la versión de Java de '$JAVA_BIN'."
fi

case "$version_line" in
  1.*) major="$(printf '%s' "${version_line#1.}" | cut -d. -f1)" ;;
  *)   major="${version_line%%.*}" ;;
esac

# --- 2. Validar la versión ---------------------------------------------------
if [ "$major" -lt 8 ] || [ "$major" -gt 15 ]; then
  fail "Java $version_line detectado (versión mayor $major).
   Este proyecto necesita JDK 11 (Temurin recomendado); el rango soportado es 8–15.
   Gradle 6.7.1 no funciona con Java 16 o superior.
   Revisa la sección \"Instalación\" del README."
fi

if [ "$major" -ne 11 ]; then
  info "⚠️  Java $version_line detectado. Funciona, pero la combinación probada es Temurin 11."
fi

vendor="$("$JAVA_BIN" -XshowSettings:properties -version 2>&1 | awk -F'= ' '/java.vendor =/ {print $2; exit}')"
case "$vendor" in
  *Microsoft*)
    info "⚠️  Estás usando el JDK de Microsoft ($vendor). LWJGL 2 falla ahí con"
    info "    \"libjawt.so: version 'SUNWprivate_1.1' not found\". Usa Temurin 11."
    ;;
esac

# --- 3. Pantalla -------------------------------------------------------------
if [ "${CODESPACES:-}" = "true" ] || [ -z "${DISPLAY:-}" ]; then
  export DISPLAY=:1
  info "Sin pantalla local: usando DISPLAY=:1."
  if [ "${CODESPACES:-}" = "true" ]; then
    info "Abre la pestaña Ports → puerto 6080 (Escritorio noVNC) y entra con la contraseña \"vscode\"."
    info "Después ejecuta este script desde la terminal del escritorio remoto."
  fi
fi

# --- 4. Ejecutar -------------------------------------------------------------
info "Iniciando CumbiaBall con Java $version_line..."
if [ "$(uname -s)" = "Darwin" ]; then
  # macOS exige el hilo principal para LWJGL 2; desktop/build.gradle lo aplica con esta propiedad.
  exec ./gradlew desktop:run -PstartOnFirstThread
else
  exec ./gradlew desktop:run
fi
