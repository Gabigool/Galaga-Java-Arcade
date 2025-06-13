import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GalagaTitleScreen extends JPanel implements KeyListener {
    private boolean startGame = false;
    private int animationCounter = 0;
    private boolean showPress = true;
    
    public GalagaTitleScreen() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        // Timer para animación del texto parpadeante
        Timer animationTimer = new Timer(500, e -> {
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
        
        // Título principal
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.drawString("GALAGA", 250, 150);
        
        // Subtítulo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("CLASSIC ARCADE SHOOTER", 220, 190);
        
        // Instrucciones de juego
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("HOW TO PLAY:", 320, 250);
        
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("← → Arrow keys to move", 280, 280);
        g.drawString("SPACE to shoot", 310, 300);
        g.drawString("R to restart during game", 290, 320);
        
        // Sistema de niveles
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("DIFFICULTY SYSTEM", 290, 370);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("• Start at Level 1 - Easy", 290, 390);
        g.drawString("• Progress through 10 levels", 290, 410);
        g.drawString("• Enemies get faster and more aggressive", 290, 430);
        g.drawString("• Maximum difficulty at Level 10", 290, 450);
        
        // Texto parpadeante para empezar
        if (showPress) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("PRESS ENTER TO START", 240, 520);
        }
        
        // Créditos
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Java Implementation - 2025", 320, 580);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            startGame = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public boolean isStartGame() {
        return startGame;
    }
    
    public void resetStartGame() {
        startGame = false;
    }
}
