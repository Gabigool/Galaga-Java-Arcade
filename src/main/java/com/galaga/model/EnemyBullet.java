package main.java.com.galaga.model;

import java.awt.*;

public class EnemyBullet {
    private int x, y;
    private static final int WIDTH = 4;
    private static final int HEIGHT = 8;
    private static final int SPEED = 3;
    
    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move() {
        y += SPEED;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public int getY() { return y; }
}
