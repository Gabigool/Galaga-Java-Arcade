package main.java.com.galaga.view;

import main.java.com.galaga.presenter.*;
import main.java.com.galaga.model.HighScore;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameView extends JPanel implements ActionListener, KeyListener {
    private GamePresenter presenter;
    private Timer timer;

    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int DELAY = 10;

    public GameView() {
        setupView();
        this.presenter = new GamePresenter();
        this.presenter.setView(this);
        startGameTimer();
    }

    private void setupView() {
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    private void startGameTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        presenter.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    public void showHighScoreDialog(int score, int level) {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NameInputDialog dialog = new NameInputDialog(parentFrame, score, level);
            dialog.setVisible(true);

            if (dialog.wasSubmitted()) {
                presenter.onHighScoreSubmitted(dialog.getPlayerName(), score, level);
            }
        });
    }

    public void showHighScoreSuccess() {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            JOptionPane.showMessageDialog(parentFrame,
                    "🎉 ¡High Score guardado exitosamente! 🎉",
                    "¡Éxito!",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void showHighScoreError() {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            JOptionPane.showMessageDialog(parentFrame,
                    "❌ Error al guardar el high score. Inténtalo de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    private void drawGame(Graphics g) {
        // Dibujar jugador
        if (presenter.getLives() > 0) {
            presenter.getPlayer().draw(g);
        }

        // Dibujar balas del jugador
        presenter.getBullets().forEach(bullet -> bullet.draw(g));

        // Dibujar enemigos
        presenter.getEnemies().forEach(enemy -> enemy.draw(g));

        // Dibujar balas enemigas
        presenter.getEnemyBullets().forEach(bullet -> bullet.draw(g));

        // Dibujar UI
        drawUI(g);

        if (presenter.isPaused()) {
            drawPauseOverlay(g);
        }

        // Dibujar Game Over si es necesario
        if (presenter.isGameOver()) {
            drawGameOver(g);
        }
    }

    private void drawPauseOverlay(Graphics g) {
        // Fondo semi-transparente
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        
        // Panel de pausa
        int panelWidth = 300;
        int panelHeight = 120;
        int panelX = (BOARD_WIDTH - panelWidth) / 2;
        int panelY = (BOARD_HEIGHT - panelHeight) / 2;
        
        // Fondo del panel
        g.setColor(new Color(40, 40, 40, 220));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
        
        // Marco del panel
        g.setColor(Color.YELLOW);
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
        g.drawRoundRect(panelX + 1, panelY + 1, panelWidth - 2, panelHeight - 2, 20, 20);
        
        // Texto de pausa
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.YELLOW);
        g.drawString("PAUSA", panelX + 70, panelY + 50);
        
        // Instrucción
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        g.drawString("Presiona P o ESC para continuar", panelX + 40, panelY + 80);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + presenter.getScore(), 10, 25);
        g.drawString("Lives: " + presenter.getLives(), 10, 45);
        g.drawString("Level: " + presenter.getLevel(), 10, 65);

        if (!presenter.isGameOver() && !presenter.isPaused()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("P/ESC: Pausar", 650, 25);
        }

        // Indicador de dificultad
        g.setColor(getDifficultyColor());
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Difficulty: " + getDifficultyName(), 650, 45);
    }

    private Color getDifficultyColor() {
        int level = presenter.getLevel();
        if (level <= 3)
            return Color.GREEN;
        else if (level <= 6)
            return Color.YELLOW;
        else if (level <= 8)
            return Color.ORANGE;
        else
            return Color.RED;
    }

    private String getDifficultyName() {
        int level = presenter.getLevel();
        if (level <= 3)
            return "Easy";
        else if (level <= 6)
            return "Medium";
        else if (level <= 8)
            return "Hard";
        else
            return "Extreme";
    }

    private void drawGameOver(Graphics g) {
        // Fondo semi-transparente
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        // Dimensiones del panel de Game Over
        int panelWidth = 500;
        int panelHeight = 280;
        int panelX = (BOARD_WIDTH - panelWidth) / 2;
        int panelY = (BOARD_HEIGHT - panelHeight) / 2;

        // Marco decorativo
        g.setColor(Color.RED);
        g.drawRect(panelX - 2, panelY - 2, panelWidth + 4, panelHeight + 4);
        g.drawRect(panelX - 1, panelY - 1, panelWidth + 2, panelHeight + 2);

        // Fondo del panel
        g.setColor(new Color(20, 20, 20, 240));
        g.fillRect(panelX, panelY, panelWidth, panelHeight);

        // Título Game Over
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.setColor(Color.BLACK);
        g.drawString("GAME OVER", panelX + 102, panelY + 52); // Sombra
        g.setColor(Color.RED);
        g.drawString("GAME OVER", panelX + 100, panelY + 50);

        // Información del juego
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + String.format("%,d", presenter.getScore()),
                panelX + 150, panelY + 90);
        g.drawString("Level Reached: " + presenter.getLevel(),
                panelX + 170, panelY + 120);

        // Separador
        g.setColor(Color.GRAY);
        g.drawLine(panelX + 50, panelY + 140, panelX + panelWidth - 50, panelY + 140);

        // Título de opciones
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.YELLOW);
        g.drawString("¿QUÉ DESEAS HACER?", panelX + 160, panelY + 165);

        // Opciones con iconos
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Opción Reiniciar
        g.setColor(Color.GREEN);
        g.fillRoundRect(panelX + 80, panelY + 180, 20, 20, 5, 5);
        g.setColor(Color.WHITE);
        g.drawString("R", panelX + 87, panelY + 194);
        g.setColor(Color.GREEN);
        g.drawString("Reiniciar Juego", panelX + 110, panelY + 194);

        // Opción Menú Principal
        g.setColor(Color.CYAN);
        g.fillRoundRect(panelX + 80, panelY + 210, 20, 20, 5, 5);
        g.setColor(Color.WHITE);
        g.drawString("M", panelX + 86, panelY + 224);
        g.setColor(Color.CYAN);
        g.drawString("Volver al Menú Principal", panelX + 110, panelY + 224);

        // Instrucción final
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Presiona la tecla correspondiente para continuar",
                panelX + 110, panelY + 255);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        presenter.onKeyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        presenter.onKeyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public GamePresenter getPresenter() {
        return presenter;
    }
}
