# 🚀 Galaga Java Arcade

Una implementación completa del clásico juego arcade **Galaga** desarrollada en Java con Swing. Incluye sistema de niveles progresivos, gráficos fieles al original y controles fluidos.

![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Status](https://img.shields.io/badge/Status-Incomplete-red.svg)

## 🎮 Características

- ✅ **Jugabilidad auténtica** - Controles y mecánicas fieles al Galaga original
- ✅ **10 niveles de dificultad** - Progresión desde fácil hasta extremo
- ✅ **Enemigos originales** - Galaga (jefe), enemigos rojos y abejas con sprites auténticos
- ✅ **Controles fluidos** - Movimiento y disparo continuo manteniendo teclas presionadas
- ✅ **Sistema de puntuación** - Puntos variables según tipo de enemigo y nivel
- ✅ **Pantalla de título** - Interfaz profesional con instrucciones
- ✅ **Animaciones** - Enemigos con alas animadas y efectos visuales

## 🕹️ Controles

| Tecla | Acción |
|-------|--------|
| `←` `→` | Mover nave (mantener presionado) |
| `ESPACIO` | Disparar (mantener presionado para disparo automático) |
| `ENTER` | Iniciar juego (pantalla de título) |
| `R` | Reiniciar juego (cuando termina) |

## 🎯 Sistema de Niveles

| Nivel | Dificultad | Velocidad Enemigos | Frecuencia Disparo | Color Indicador |
|-------|------------|-------------------|-------------------|-----------------|
| 1-3   | Fácil      | Base              | Baja              | 🟢 Verde       |
| 4-6   | Medio      | +30%              | Media             | 🟡 Amarillo    |
| 7-8   | Difícil    | +60%              | Alta              | 🟠 Naranja     |
| 9-10  | Extremo    | +90%              | Muy Alta          | 🔴 Rojo        |

## 🏆 Sistema de Puntuación

- **Abeja (Amarilla):** 100 + (nivel × 50) puntos
- **Enemigo Rojo:** 160 + (nivel × 50) puntos  
- **Galaga (Jefe Azul):** 400 + (nivel × 50) puntos
- **Bonus por nivel:** 1000 × nivel completado

## 🚀 Instalación y Ejecución

### Requisitos
- Java 8 o superior
- No requiere dependencias externas

### Ejecución
1. Ve a [Releases](https://github.com/Gabriel-dev-hub/Galaga-Java-Arcade/tree/main/Releases)
2. Descarga `GalagaGame.jar`
3. Asegúrate de tener Java 8+ instalado
4. Ejecuta con doble clic o: `java -jar galaga-game.jar`
