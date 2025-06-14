package main.java.com.galaga.controller;

public class AppConfig {
    // Configuración de ventana
    public static final String APP_TITLE = "Galaga - Classic Arcade";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    
    // Configuración de juego
    public static final int GAME_TIMER_DELAY = 10;
    public static final int STATE_CHECK_DELAY = 100;
    
    // Configuración de debug
    public static final boolean DEBUG_MODE = false;
    
    public static void log(String message) {
        if (DEBUG_MODE) {
            System.out.println("[DEBUG] " + message);
        }
    }
}