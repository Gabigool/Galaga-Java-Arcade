package main.java.com.galaga.model;

import java.awt.*;

public class Enemy {
    private int x, y;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private double speed = 1; // Cambiado a double para velocidad variable
    private int direction = 1;
    private EnemyType type;
    private int animationFrame = 0;
    private int animationCounter = 0;
    
    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public void move() {
        x += speed * direction;
        if (x <= 0 || x >= 800 - WIDTH) {
            direction *= -1;
            y += 20;
        }
        
        animationCounter++;
        if (animationCounter > 30) {
            animationFrame = (animationFrame + 1) % 2;
            animationCounter = 0;
        }
    }
    
    // El resto del código de draw() permanece igual...
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        switch (type) {
            case BOSS:
                drawBoss(g2d);
                break;
            case RED:
                drawRedEnemy(g2d);
                break;
            case BEE:
                drawBee(g2d);
                break;
        }
    }
    
    private void drawBoss(Graphics2D g) {
        g.setColor(new Color(0, 100, 255));
        g.fillOval(x + 5, y + 8, 20, 14);
        
        if (animationFrame == 0) {
            g.fillOval(x, y + 10, 8, 8);
            g.fillOval(x + 22, y + 10, 8, 8);
        } else {
            g.fillOval(x + 2, y + 12, 6, 6);
            g.fillOval(x + 22, y + 12, 6, 6);
        }
        
        g.setColor(Color.WHITE);
        g.fillOval(x + 8, y + 10, 3, 3);
        g.fillOval(x + 19, y + 10, 3, 3);
        
        g.setColor(Color.YELLOW);
        g.fillOval(x + 10, y + 5, 2, 3);
        g.fillOval(x + 18, y + 5, 2, 3);
    }
    
    private void drawRedEnemy(Graphics2D g) {
        g.setColor(new Color(255, 50, 50));
        g.fillOval(x + 8, y + 10, 14, 10);
        
        if (animationFrame == 0) {
            g.fillOval(x + 3, y + 12, 6, 6);
            g.fillOval(x + 21, y + 12, 6, 6);
        } else {
            g.fillOval(x + 5, y + 14, 4, 4);
            g.fillOval(x + 21, y + 14, 4, 4);
        }
        
        g.setColor(Color.WHITE);
        g.fillOval(x + 10, y + 12, 2, 2);
        g.fillOval(x + 18, y + 12, 2, 2);
    }
    
    private void drawBee(Graphics2D g) {
        g.setColor(new Color(255, 255, 0));
        g.fillOval(x + 10, y + 12, 10, 8);
        
        g.setColor(Color.BLACK);
        g.fillRect(x + 11, y + 13, 1, 6);
        g.fillRect(x + 13, y + 13, 1, 6);
        g.fillRect(x + 15, y + 13, 1, 6);
        g.fillRect(x + 17, y + 13, 1, 6);
        
        g.setColor(Color.WHITE);
        if (animationFrame == 0) {
            g.fillOval(x + 6, y + 10, 4, 6);
            g.fillOval(x + 20, y + 10, 4, 6);
        } else {
            g.fillOval(x + 7, y + 8, 3, 8);
            g.fillOval(x + 20, y + 8, 3, 8);
        }
        
        g.setColor(Color.BLACK);
        g.fillOval(x + 11, y + 14, 2, 2);
        g.fillOval(x + 17, y + 14, 2, 2);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public EnemyType getType() { return type; }
}
