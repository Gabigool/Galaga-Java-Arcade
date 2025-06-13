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
        
        // Dibujar Game Over si es necesario
        if (presenter.isGameOver()) {
            drawGameOver(g);
        }
    }
    
    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + presenter.getScore(), 10, 25);
        g.drawString("Lives: " + presenter.getLives(), 10, 45);
        g.drawString("Level: " + presenter.getLevel(), 10, 65);
        
        // Indicador de dificultad
        g.setColor(getDifficultyColor());
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Difficulty: " + getDifficultyName(), 600, 25);
    }
    
    private Color getDifficultyColor() {
        int level = presenter.getLevel();
        if (level <= 3) return Color.GREEN;
        else if (level <= 6) return Color.YELLOW;
        else if (level <= 8) return Color.ORANGE;
        else return Color.RED;
    }
    
    private String getDifficultyName() {
        int level = presenter.getLevel();
        if (level <= 3) return "Easy";
        else if (level <= 6) return "Medium";
        else if (level <= 8) return "Hard";
        else return "Extreme";
    }
    
    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.RED);
        g.drawString("GAME OVER", BOARD_WIDTH/2 - 150, BOARD_HEIGHT/2 - 50);
        
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + presenter.getScore(), BOARD_WIDTH/2 - 80, BOARD_HEIGHT/2);
        g.drawString("Level Reached: " + presenter.getLevel(), BOARD_WIDTH/2 - 90, BOARD_HEIGHT/2 + 30);
        g.drawString("Press R to Restart", BOARD_WIDTH/2 - 100, BOARD_HEIGHT/2 + 80);
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
    public void keyTyped(KeyEvent e) {}
}
