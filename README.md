# Induztek SpA - Gestión de Protocolos

Aplicación móvil Android desarrollada para la **formalización inmediata de protocolos de prueba eléctrica en terreno**, diseñada específicamente para el caso de estudio de Induztek Ingeniería SpA.

## Descripción del Proyecto

Esta aplicación permite a los técnicos e ingenieros de Induztek SpA registrar pruebas eléctricas de equipos primarios y secundarios (transformadores, relés, interruptores) directamente en terreno, garantizando la persistencia de datos **100% offline** y la trazabilidad de la información.

El proyecto cumple con los requerimientos técnicos de arquitectura modular, separación de responsabilidades y validaciones en tiempo real solicitados en la rúbrica de evaluación.

## Características Principales

*   **Arquitectura Moderna:** Implementación estricta del patrón **MVVM** (Model-View-ViewModel) para separar la interfaz gráfica de la lógica de negocio.
*   **Interfaz Declarativa:** Construida 100% con **Jetpack Compose** y los lineamientos de **Material Design 3**.
*   **Funcionamiento Offline:** Persistencia de datos local utilizando **SQLite** (`SQLiteOpenHelper`) para asegurar cero pérdida de datos en zonas sin señal (salas eléctricas, minería).
*   **Seguridad y Accesos:** Sistema de login con validaciones mediante Expresiones Regulares (Regex) y manejo de estados dinámicos.
*   **Gestión de Roles:** Vistas dinámicas dependiendo del perfil del usuario (Jefe Técnico, Ingeniero, Técnico, Cliente).
*   **Hardware Integrado:** Acceso nativo y seguro a la **Cámara** y **Almacenamiento** del dispositivo para personalizar el perfil del usuario (fotos convertidas y almacenadas en formato Base64).
*   **Retroalimentación Visual:** Animaciones fluidas (`AnimatedVisibility`) e íconos dinámicos en los formularios de ingreso.

##  Stack Tecnológico

*   **Lenguaje:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Navegación:** Navigation Compose
*   **Arquitectura:** MVVM + StateFlow + Corrutinas
*   **Base de Datos:** SQLite Nativo
*   **Preferencias:** SharedPreferences (Gestión de Sesión)

## Cuentas de Prueba

Para revisar las funcionalidades con privilegios elevados (gestión de usuarios y visión global de protocolos), utilice las siguientes credenciales predeterminadas:

*   **Usuario (Correo):** `admin@induztek.cl`
*   **Contraseña:** `Admin123!`
*   **Rol:** Jefe técnico (Administrador)

> *Nota: También puede registrar un nuevo usuario desde la pantalla de inicio para probar el flujo de "Técnico".*

## Instalación y Uso

1. Clonar el repositorio.
2. Abrir el proyecto en **Android Studio**.
3. Sincronizar Gradle.
4. Ejecutar en un emulador o dispositivo físico con Android 8.0 (API 26) o superior (Recomendado API 31+).

---
*Desarrollado como proyecto académico.*
