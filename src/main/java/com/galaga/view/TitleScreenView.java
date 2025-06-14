package main.java.com.galaga.view;

import main.java.com.galaga.model.HighScore;
import main.java.com.galaga.presenter.TitleScreenPresenter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class TitleScreenView extends JPanel implements KeyListener {
    private TitleScreenPresenter presenter;
    private boolean showPress = true;
    private Timer animationTimer;

    // Estados de pantalla
    private boolean showingMainMenu = true;
    private boolean showingHighScores = false;
    private boolean showingInstructions = false;
    private List<HighScore> highScores;

    public TitleScreenView() {
        setupView();
        this.presenter = new TitleScreenPresenter();
        this.presenter.setView(this);
        startAnimation();
    }

    private void setupView() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    private void startAnimation() {
        animationTimer = new Timer(500, e -> {
            showPress = !showPress;
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (showingMainMenu) {
            drawMainMenu(g2d);
        } else if (showingHighScores) {
            drawHighScoresScreen(g2d);
        } else if (showingInstructions) {
            drawInstructionsScreen(g2d);
        }
    }

    private void drawMainMenu(Graphics2D g) {
        // Título principal
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.drawString("GALAGA", 250, 120);

        // Subtítulo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("CLASSIC ARCADE SHOOTER", 220, 160);

        // Opciones del menú
        String[] options = presenter.getMenuOptions();
        int selectedOption = presenter.getSelectedOption();

        g.setFont(new Font("Arial", Font.BOLD, 28));
        for (int i = 0; i < options.length; i++) {
            int y = 250 + (i * 60);

            // Destacar opción seleccionada
            if (i == selectedOption) {
                // Fondo de selección
                g.setColor(new Color(0, 100, 255, 100));
                g.fillRoundRect(180, y - 35, 440, 45, 10, 10);

                // Texto seleccionado
                g.setColor(Color.YELLOW);
                if (showPress) { // Efecto parpadeante en la opción seleccionada
                    g.drawString("► " + options[i] + " ◄", 200, y);
                } else {
                    g.drawString(options[i], 220, y);
                }
            } else {
                // Texto normal
                g.setColor(Color.WHITE);
                g.drawString(options[i], 220, y);
            }
        }

        // Instrucciones de navegación
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("↑↓ Navegar  |  ENTER/SPACE Seleccionar  |  ESC Volver", 200, 550);

        // Créditos
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Java Implementation - 2025", 320, 580);
    }

    private void drawHighScoresScreen(Graphics2D g) {
        // Título
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("HIGH SCORES", 150, 80);

        // Lista de high scores
        if (highScores != null && !highScores.isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("RANK", 50, 140);
            g.drawString("PLAYER", 150, 140);
            g.drawString("SCORE", 350, 140);
            g.drawString("LEVEL", 500, 140);
            g.drawString("DATE", 600, 140);

            // Línea separadora
            g.setColor(Color.GRAY);
            g.drawLine(50, 150, 750, 150);

            // Mostrar cada high score
            for (int i = 0; i < highScores.size() && i < 10; i++) {
                HighScore score = highScores.get(i);
                int y = 180 + (i * 35);

                // Color alternado para las filas
                if (i % 2 == 0) {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(40, y - 20, 720, 30);
                }

                // Color según la posición
                if (i == 0) {
                    g.setColor(Color.YELLOW); // Oro
                } else if (i == 1) {
                    g.setColor(Color.LIGHT_GRAY); // Plata
                } else if (i == 2) {
                    g.setColor(new Color(205, 127, 50)); // Bronce
                } else {
                    g.setColor(Color.WHITE);
                }

                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("#" + (i + 1), 50, y);
                g.drawString(score.getPlayerName(), 150, y);
                g.drawString(String.format("%,d", score.getScore()), 350, y);
                g.drawString("" + score.getLevelReached(), 500, y);
                g.drawString(score.getFormattedDate(), 600, y);
            }
        } else {
            // No hay high scores
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 24));
            g.drawString("No hay puntajes registrados aún", 250, 300);
            g.drawString("¡Sé el primero en jugar!", 280, 340);
        }

        // Instrucciones
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Presiona ESC para volver al menú principal", 220, 520);
    }

    private void drawInstructionsScreen(Graphics2D g) {
        // Título
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("CÓMO JUGAR", 180, 80);

        // Controles
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("CONTROLES:", 50, 140);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("← → Flechas izquierda/derecha: Mover la nave", 70, 170);
        g.drawString("ESPACIO: Disparar (mantén presionado para disparo continuo)", 70, 200);
        g.drawString("P / ESC: Pausar/Reanudar juego", 70, 230);
        g.drawString("R: Reiniciar juego (durante Game Over)", 70, 260);

        // Objetivo
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("OBJETIVO:", 50, 280);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("• Destruye todas las naves enemigas para avanzar de nivel", 70, 310);
        g.drawString("• Evita que las balas enemigas te golpeen", 70, 340);
        g.drawString("• Sobrevive el mayor tiempo posible para obtener más puntos", 70, 370);

        // Sistema de puntuación
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("PUNTUACIÓN:", 50, 420);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Galaga (Jefe): 400 puntos + bonus de nivel", 70, 450);
        g.drawString("Enemigo Rojo: 160 puntos + bonus de nivel", 70, 480);
        g.drawString("Abeja: 100 puntos + bonus de nivel", 70, 510);

        // Instrucciones
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Presiona ESC para volver al menú principal", 220, 560);
    }

    // Métodos llamados por el presenter
    public void updateSelectedOption(int selectedOption) {
        repaint();
    }

    public void showMainMenu() {
        showingMainMenu = true;
        showingHighScores = false;
        showingInstructions = false;
        repaint();
    }

    public void showHighScoresScreen(List<HighScore> scores) {
        this.highScores = scores;
        showingMainMenu = false;
        showingHighScores = true;
        showingInstructions = false;
        repaint();
    }

    public void showInstructionsScreen() {
        showingMainMenu = false;
        showingHighScores = false;
        showingInstructions = true;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        presenter.onKeyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean isStartGame() {
        return presenter.isStartGame();
    }

    public void resetStartGame() {
        presenter.resetStartGame();
    }
}
