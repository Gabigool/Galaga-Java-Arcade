import main.java.com.galaga.view.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Galaga - Classic Arcade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        // Mostrar primero la pantalla de título
        TitleScreenView titleScreen = new TitleScreenView();
        frame.add(titleScreen);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Timer para verificar cuando empezar el juego
        Timer checkStart = new Timer(100, e -> {
            if (titleScreen.isStartGame()) {
                frame.remove(titleScreen);
                GameView game = new GameView();
                frame.add(game);
                frame.revalidate();
                frame.repaint();
                game.requestFocusInWindow();
                ((Timer) e.getSource()).stop();
            }
        });
        checkStart.start();
    }
}
