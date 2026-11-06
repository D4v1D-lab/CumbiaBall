# 🎮 CumbiaBall

Un juego de plataformas 2D hecho con [libGDX](https://libgdx.com/): controlas un balón que salta de plataforma en plataforma, esquiva pinchos y llega a la meta mientras suena música de cumbia. Hay dos niveles y una pantalla de "game over" con reintento.

<!-- Si tienes una captura o GIF del juego, guárdalo en docs/captura.gif y descomenta la línea siguiente. -->
<!-- ![Captura de CumbiaBall](docs/captura.gif) -->

---

## ✅ Requisitos

| Qué necesitas | Detalle |
|---|---|
| **Java** | **JDK 11 — Temurin recomendado** (probado con 11.0.x) |
| Rango soportado | Java 8 a 15. Fuera de ese rango el proyecto no arranca |
| ⚠️ Evita | Java 16 o superior, y el **JDK de Microsoft** (ver solución de problemas) |
| Sistema | Windows, macOS, Linux o GitHub Codespaces |
| Gradle | No hace falta instalarlo: el repo trae el wrapper (`./gradlew`) |

> ⚠️ **No uses Java 16, 17, 21 ni 25.** El proyecto usa Gradle 6.7.1, que solo funciona hasta Java 15. Si ves `Unsupported class file major version`, es exactamente eso.

---

## ☁️ Opción A — Jugar en GitHub Codespaces (sin instalar nada)

1. Entra al repo y pulsa **Code → Codespaces → Create codespace on master**.
2. Espera a que termine de construirse el contenedor (la primera vez tarda unos minutos: instala Java 11 y las librerías gráficas).
3. Abre la pestaña **Ports** (junto a la Terminal) y busca el puerto **6080 — Escritorio noVNC**. Pulsa el icono del globo para abrirlo.
4. Ingresa la contraseña: **`vscode`**.
5. En el escritorio remoto, abre una terminal (icono de terminal en la barra o clic derecho → *Open Terminal Here*) y ejecuta:

   ```bash
   ./scripts/run.sh
   ```

   El juego aparecerá en esa misma ventana remota.

> 💡 También puedes lanzarlo desde la terminal normal de VS Code: el contenedor ya trae `DISPLAY=:1`, así que la ventana se dibuja en el escritorio de noVNC.
>
> 🔇 En Codespaces no hay tarjeta de sonido. Verás `Couldn't initialize audio` y el juego seguirá funcionando sin sonido: es lo esperado.

---

## 💻 Opción B — Jugar en tu computador

### 1. Instala Java 11 (Temurin)

**Windows**

```powershell
winget install EclipseAdoptium.Temurin.11.JDK
```

**macOS** (con Homebrew)

```bash
brew install --cask temurin@11
```

**Linux (Debian/Ubuntu)** — descarga el `.tar.gz` de [Adoptium](https://adoptium.net/temurin/releases/?version=11), descomprímelo y apunta `JAVA_HOME` a esa carpeta.

**Cualquier sistema con [SDKMAN!](https://sdkman.io/)**

```bash
sdk env install     # lee .sdkmanrc y baja Temurin 11
sdk env             # activa esa versión en la sesión actual
```

Comprueba que quedó bien:

```bash
java -version
# openjdk version "11.0.x" ...
```

### 2. Clona el repositorio

```bash
git clone https://github.com/D4v1D-lab/CumbiaBall.git
cd CumbiaBall
```

### 3. Ejecuta el juego

**Linux / macOS**

```bash
./scripts/run.sh
```

**Windows**

```bat
scripts\run.bat
```

El script verifica tu versión de Java, avisa si algo no cuadra y arranca `./gradlew desktop:run`. Si prefieres hacerlo a mano: `./gradlew desktop:run`.

---

## 🕹️ Controles

| Acción | Cómo |
|---|---|
| Saltar | **Clic** o **toque** en cualquier parte de la pantalla |
| Saltar al aterrizar | Mantén pulsado mientras caes: el salto se ejecuta al tocar el suelo |
| Iniciar partida | Clic en el botón **play** del menú |
| Reintentar tras perder | Clic en el botón de la pantalla de *game over* |
| Salir | Cierra la ventana del juego |

Objetivo: avanzar por las plataformas sin tocar los pinchos y alcanzar la bandera de meta. Al superar el primer nivel pasas al segundo, con otra música.

---

## 🔧 Solución de problemas

**`Unsupported class file major version 69`**
Estás usando Java 25 (o cualquier versión 16+). Gradle 6.7.1 no puede ejecutarse ahí. Instala Temurin 11 y asegúrate de que `java -version` lo confirme. Si usas SDKMAN, ejecuta `sdk env`.

**`libjawt.so: version 'SUNWprivate_1.1' not found`**
Estás con el JDK de Microsoft (`msopenjdk`). LWJGL 2 no es compatible con ese build. Desinstálalo o cambia `JAVA_HOME` a un Temurin 11 (o Zulu 11) y detén el daemon: `./gradlew --stop`.

**`libXcursor.so.1: cannot open shared object file`**
Faltan librerías gráficas de X11. En Debian/Ubuntu:

```bash
sudo apt-get update && sudo apt-get install -y \
  libxcursor1 libxrandr2 libxxf86vm1 libgl1-mesa-dri libopenal1 libgl1 mesa-utils
```

En Codespaces esto ya lo instala el `postCreateCommand` del devcontainer. Si acabas de cambiar el devcontainer, reconstruye el contenedor (*Command Palette → Codespaces: Rebuild Container*).

**`Could not open X display connection` / pantalla en negro en noVNC**
No hay servidor gráfico disponible. En Codespaces: confirma que el puerto **6080** está abierto, entra con la contraseña `vscode` y ejecuta el juego **dentro** del escritorio remoto. En Linux sin entorno gráfico, define `export DISPLAY=:1` solo si tienes un servidor X (Xvfb o noVNC) corriendo ahí. En macOS, el script ya añade `-XstartOnFirstThread`.

**`Couldn't initialize audio`**
Es normal en Codespaces y en equipos sin tarjeta de sonido. El juego se ejecuta igual, sin audio; no es un error del proyecto.

**Cambié de Java y sigue fallando con el Java viejo**
El daemon de Gradle conserva el JDK con el que se inició. Deténlo y vuelve a lanzar:

```bash
./gradlew --stop
./scripts/run.sh
```

**VS Code no reconoce Java 11**
El repo no fija rutas de JDK (para no romper en otras máquinas). Abre la paleta de comandos → **Java: Configure Java Runtime** y selecciona tu Temurin 11, o define `JAVA_HOME` antes de abrir VS Code. Las extensiones recomendadas se instalan solas al abrir el proyecto.

---

## 📁 Estructura del proyecto

```
CumbiaBall/
├── core/                 Lógica del juego (pantallas, entidades, física Box2D)
├── desktop/              Launcher de escritorio (LWJGL 2) y tarea `run`
├── android/              Módulo Android y assets del juego (android/assets)
├── assets/               Imágenes sueltas usadas por el módulo Android
├── scripts/              run.sh (Linux/macOS/Codespaces) y run.bat (Windows)
├── .devcontainer/        Entorno reproducible de Codespaces
├── .vscode/              Extensiones y configuración recomendadas
├── build.gradle          Configuración raíz y verificación del JDK
└── gradlew, gradlew.bat  Gradle Wrapper 6.7.1
```

> ℹ️ El módulo `desktop` toma las imágenes y sonidos de `android/assets`, por eso el `workingDir` de la tarea `run` apunta ahí.

---

## 🤝 Contribuir

1. Crea una rama: `git checkout -b mi-cambio`.
2. Haz tus cambios y pruébalos con `./scripts/run.sh`.
3. Abre un Pull Request describiendo qué cambiaste y cómo lo probaste.

No actualices Gradle ni las versiones de libGDX sin discutirlo antes: el proyecto está fijado a versiones antiguas a propósito y migrarlo implica cambios mayores.

## 📄 Licencia

El repositorio no incluye un archivo de licencia. Si vas a reutilizar el código o los recursos, consulta antes con el autor.
