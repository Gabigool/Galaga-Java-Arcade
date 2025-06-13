import java.awt.*;

public class Player {
    private int x, y;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 30;
    private static final int SPEED = 3; // Velocidad reducida para movimiento más suave
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void moveLeft() {
        if (x > 0) {
            x -= SPEED;
        }
    }
    
    public void moveRight() {
        if (x < 800 - WIDTH) {
            x += SPEED;
        }
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.WHITE);
        
        // Cuerpo principal
        g.fillRect(x + 20, y + 15, 10, 15);
        
        // Punta de la nave
        int[] xPoints = {x + 25, x + 15, x + 35};
        int[] yPoints = {y, y + 15, y + 15};
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Alas
        g.fillRect(x + 5, y + 20, 15, 8);
        g.fillRect(x + 30, y + 20, 15, 8);
        
        // Detalles azules
        g.setColor(new Color(0, 150, 255));
        g.fillRect(x + 22, y + 17, 6, 3);
        g.fillRect(x + 7, y + 22, 11, 4);
        g.fillRect(x + 32, y + 22, 11, 4);
        
        // Motor
        g.setColor(Color.RED);
        g.fillRect(x + 23, y + 25, 4, 5);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
}
