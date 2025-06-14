package main.java.com.galaga.controller;

import main.java.com.galaga.view.*;
import javax.swing.*;

public class ApplicationController {
    private JFrame frame;
    private TitleScreenView titleScreen;
    private GameView gameView;
    private Timer gameStateMonitor;
    
    public ApplicationController() {
        initializeApplication();
    }
    
    private void initializeApplication() {
        setupLookAndFeel();
        createMainWindow();
        showTitleScreen();
        startGameStateMonitoring();
    }
    
    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }
    }
    
    private void createMainWindow() {
        frame = new JFrame("Galaga - Classic Arcade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        // NO llamar setLocationRelativeTo aquí todavía
    }
    
    private void showTitleScreen() {
        clearCurrentScreen();
        
        titleScreen = new TitleScreenView();
        frame.add(titleScreen);
        
        // CORREGIDO - Centrar después de establecer el contenido
        centerAndShowWindow();
        titleScreen.requestFocusInWindow();
        
    }
    
    private void showGameScreen() {
        clearCurrentScreen();
        
        gameView = new GameView();
        frame.add(gameView);
        
        // CORREGIDO - Solo refrescar, no volver a centrar
        refreshScreenContent();
        gameView.requestFocusInWindow();
        
    }
    
    private void clearCurrentScreen() {
        if (titleScreen != null) {
            frame.remove(titleScreen);
            titleScreen = null;
        }
        if (gameView != null) {
            frame.remove(gameView);
            gameView = null;
        }
    }
    
    /**
     * NUEVO MÉTODO - Centra y muestra la ventana correctamente
     */
    private void centerAndShowWindow() {
        frame.pack(); // Calcular tamaño necesario
        frame.setLocationRelativeTo(null); // Centrar en pantalla
        frame.setVisible(true); // Mostrar ventana
    }
    
    /**
     * NUEVO MÉTODO - Solo refresca el contenido sin reposicionar
     */
    private void refreshScreenContent() {
        frame.revalidate();
        frame.repaint();
    }
    
    private void startGameStateMonitoring() {
        gameStateMonitor = new Timer(100, e -> checkScreenTransitions());
        gameStateMonitor.start();
    }
    
    private void checkScreenTransitions() {
        // Verificar transición: Título → Juego
        if (titleScreen != null && titleScreen.isStartGame()) {
            titleScreen.resetStartGame();
            showGameScreen();
        }
        
        // Verificar transición: Juego → Título
        if (gameView != null && gameView.getPresenter().shouldReturnToMenu()) {
            gameView.getPresenter().resetReturnToMenu();
            showTitleScreen();
        }
    }
    
    /**
     * Método público para iniciar la aplicación
     */
    public void start() {
        // Ya no es necesario hacer nada aquí, 
        // la ventana se muestra en showTitleScreen()
    }
    
    /**
     * Método para cerrar la aplicación limpiamente
     */
    public void shutdown() {
        if (gameStateMonitor != null) {
            gameStateMonitor.stop();
        }
        System.exit(0);
    }
}
