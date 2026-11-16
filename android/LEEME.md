# Módulo Android (heredado)

Este módulo **no forma parte del build** (ver `settings.gradle`). Se dejó en el
repositorio como referencia, pero no compila con el stack actual:

- Usa **Android Gradle Plugin 3.5.4** (2019), incompatible con Gradle 9.
- `compileSdkVersion` / `targetSdkVersion` 30 y `minSdkVersion` 14 están fuera de
  los requisitos actuales de Google Play (target 35+).
- Usa el DSL antiguo (`buildToolsVersion`, `packagingOptions`), eliminado en AGP 9.

## Para reactivarlo

1. Migrar `android/build.gradle` a **AGP 9.x**: `namespace`, `compileSdk 35`,
   `minSdk 24`, `targetSdk 35`, bloque `packaging` y `buildFeatures`.
2. Mover los assets a `assets/` (convención actual de libGDX) o mantener
   `android/assets` y actualizar las rutas.
3. Volver a incluir `android` en `settings.gradle` y probar con un dispositivo o
   emulador.

Mientras tanto, el juego funciona en escritorio con `./scripts/run.sh`.
