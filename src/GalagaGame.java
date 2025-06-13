import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public class GalagaGame extends JPanel implements ActionListener, KeyListener {
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int DELAY = 10;
    private static final int SHOOT_DELAY = 150;
    
    private Timer timer;
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyBullet> enemyBullets;
    private int score;
    private boolean gameOver;
    private int lives;
    private int level; // Nuevo: sistema de niveles
    
    private Set<Integer> pressedKeys;
    private long lastShootTime;
    
    public GalagaGame() {
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        pressedKeys = new HashSet<>();
        lastShootTime = 0;
        
        initGame();
    }
    
    private void initGame() {
        player = new Player(BOARD_WIDTH / 2 - 25, BOARD_HEIGHT - 80);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        score = 0;
        gameOver = false;
        lives = 3;
        level = 1; // Empezar en nivel 1
        
        createEnemies();
        
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void restartGame() {
        pressedKeys.clear();
        initGame();
    }
    
    private void createEnemies() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                int x = col * 60 + 50;
                int y = row * 50 + 50;
                
                EnemyType type;
                if (row == 0) {
                    type = EnemyType.BOSS;
                } else if (row <= 2) {
                    type = EnemyType.RED;
                } else {
                    type = EnemyType.BEE;
                }
                
                Enemy enemy = new Enemy(x, y, type);
                // Aumentar velocidad basada en el nivel
                enemy.setSpeed(1 + (level - 1) * 0.3);
                enemies.add(enemy);
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        if (lives > 0) {
            player.draw(g);
        }
        
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        
        for (EnemyBullet enemyBullet : enemyBullets) {
            enemyBullet.draw(g);
        }
        
        // UI mejorada
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 25);
        g.drawString("Lives: " + lives, 10, 45);
        g.drawString("Level: " + level, 10, 65); // Mostrar nivel actual
        
        // Indicador de dificultad
        g.setColor(getDifficultyColor());
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Difficulty: " + getDifficultyName(), 600, 25);
        
        if (gameOver) {
            // Pantalla de Game Over mejorada
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
            
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", BOARD_WIDTH/2 - 150, BOARD_HEIGHT/2 - 50);
            
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Final Score: " + score, BOARD_WIDTH/2 - 80, BOARD_HEIGHT/2);
            g.drawString("Level Reached: " + level, BOARD_WIDTH/2 - 90, BOARD_HEIGHT/2 + 30);
            g.drawString("Press R to Restart", BOARD_WIDTH/2 - 100, BOARD_HEIGHT/2 + 80);
        }
    }
    
    private Color getDifficultyColor() {
        if (level <= 3) return Color.GREEN;
        else if (level <= 6) return Color.YELLOW;
        else if (level <= 8) return Color.ORANGE;
        else return Color.RED;
    }
    
    private String getDifficultyName() {
        if (level <= 3) return "Easy";
        else if (level <= 6) return "Medium";
        else if (level <= 8) return "Hard";
        else return "Extreme";
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            handleContinuousInput();
            update();
        }
        repaint();
    }
    
    private void handleContinuousInput() {
        long currentTime = System.currentTimeMillis();
        
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            player.moveLeft();
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            player.moveRight();
        }
        
        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            if (currentTime - lastShootTime >= SHOOT_DELAY) {
                bullets.add(new Bullet(player.getX() + 22, player.getY()));
                lastShootTime = currentTime;
            }
        }
    }
    
    private void update() {
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.move();
            if (bullet.getY() < 0) {
                bulletIter.remove();
            }
        }
        
        for (Enemy enemy : enemies) {
            enemy.move();
            // Frecuencia de disparo aumenta con el nivel
            double shootChance = 0.0005 * (1 + level * 0.5);
            if (Math.random() < shootChance) {
                enemyBullets.add(new EnemyBullet(enemy.getX() + 15, enemy.getY() + 30));
            }
        }
        
        Iterator<EnemyBullet> enemyBulletIter = enemyBullets.iterator();
        while (enemyBulletIter.hasNext()) {
            EnemyBullet enemyBullet = enemyBulletIter.next();
            enemyBullet.move();
            if (enemyBullet.getY() > BOARD_HEIGHT) {
                enemyBulletIter.remove();
            }
        }
        
        checkCollisions();
    }
    
    private void checkCollisions() {
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bulletIter.remove();
                    enemyIter.remove();
                    
                    // Puntuación aumenta con el nivel
                    int levelBonus = (level - 1) * 50;
                    switch (enemy.getType()) {
                        case BOSS:
                            score += 400 + levelBonus;
                            break;
                        case RED:
                            score += 160 + levelBonus;
                            break;
                        case BEE:
                            score += 100 + levelBonus;
                            break;
                    }
                    break;
                }
            }
        }
        
        Iterator<EnemyBullet> enemyBulletIter = enemyBullets.iterator();
        while (enemyBulletIter.hasNext()) {
            EnemyBullet enemyBullet = enemyBulletIter.next();
            if (enemyBullet.getBounds().intersects(player.getBounds())) {
                enemyBulletIter.remove();
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                    timer.stop();
                }
                break;
            }
        }
        
        // Avanzar al siguiente nivel
        if (enemies.isEmpty()) {
            if (level < 10) {
                level++;
                // Bonus por completar nivel
                score += level * 1000;
            }
            createEnemies();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.add(keyCode);
        
        if (keyCode == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.remove(keyCode);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
