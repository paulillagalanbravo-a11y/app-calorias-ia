# App Calorías IA

Proyecto Android en Kotlin con Jetpack Compose, arquitectura MVVM, navegación básica y persistencia local con Room.

## Requisitos
- Android Studio Giraffe o superior (recomendado Koala).
- JDK 17 configurado en Android Studio.
- SDK Android 34 descargado.

## Abrir el proyecto en Android Studio
1. Abre Android Studio.
2. Selecciona **Open** y elige la carpeta del proyecto `app-calorias-ia`.
3. Espera a que Gradle sincronice las dependencias.

## Ejecutar la app
1. Conecta un dispositivo Android o inicia un emulador.
2. Selecciona el módulo **app**.
3. Presiona **Run** (botón ▶) o usa `Shift + F10`.

## Estructura del proyecto
- `app/src/main/java/com/example/appcaloriasia/ui/`: pantallas Compose y navegación.
- `app/src/main/java/com/example/appcaloriasia/viewmodel/`: ViewModels (MVVM).
- `app/src/main/java/com/example/appcaloriasia/data/`: entidades, DAO, base de datos y repositorios (Room).
- `app/src/main/res/`: recursos (strings y temas).
