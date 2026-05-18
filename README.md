# EDDY-Parcial-2-

## Configuracion en otra PC

Este proyecto no versiona `local.properties` porque ese archivo depende de cada maquina.

## Requisitos para compilar (Windows y macOS)

1. Android Studio actualizado.
2. Android SDK instalado.
3. JDK **17** o **21** configurado como Gradle JDK (no usar JDK 25).

> Si usas JDK 25, Gradle puede fallar con errores como `25.0.2`.

Si al abrir el proyecto aparece un error de Android SDK:

1. Presiona **OK** en el mensaje de Android Studio.
2. Abre `File > Settings > Android SDK` (en macOS: `Android Studio > Settings > Android SDK`) y valida la ruta del SDK.
3. Sincroniza Gradle de nuevo.

Si falla el build por Java:

1. Abre `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`.
2. En `Gradle JDK`, selecciona JDK 17 o 21 (recomendado: Embedded JDK de Android Studio si es 17/21).
3. Vuelve a sincronizar el proyecto.

Rutas comunes del SDK:

- Windows: `C:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk`
- macOS: `/Users/TU_USUARIO/Library/Android/sdk`