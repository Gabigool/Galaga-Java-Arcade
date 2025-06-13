import java.awt.*;

public class Bullet {
    private int x, y;
    private static final int WIDTH = 4;
    private static final int HEIGHT = 10;
    private static final int SPEED = 7;
    
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move() {
        y -= SPEED;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public int getY() { return y; }
}
