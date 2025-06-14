package main.java.com.galaga.presenter;

import main.java.com.galaga.view.*;
import main.java.com.galaga.model.*;
import main.java.com.galaga.persistence.repository.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class GamePresenter {
    private GameView view;

    // Estado del juego (lógica de negocio)
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyBullet> enemyBullets;
    private Set<Integer> pressedKeys;

    private int score;
    private int level;
    private int lives;
    private boolean gameOver;
    private long lastShootTime;

    private static final int SHOOT_DELAY = 150;
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;

    private boolean returnToMenu = false;
    private boolean isPaused = false;

    private HighScoreRepository highScoreRepository;

    public GamePresenter() {
        this.pressedKeys = new HashSet<>();
        this.highScoreRepository = new HighScoreRepository();
        initGame();
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void initGame() {
        player = new Player(BOARD_WIDTH / 2 - 25, BOARD_HEIGHT - 80);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        score = 0;
        level = 1;
        lives = 3;
        gameOver = false;
        lastShootTime = 0;

        createEnemies();
    }

    public void restartGame() {
        pressedKeys.clear();
        isPaused = false;
        initGame();
    }

    private void createEnemies() {
        enemies.clear();
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
                enemy.setSpeed(1 + (level - 1) * 0.3);
                enemies.add(enemy);
            }
        }
    }

    private void handleContinuousInput() {
        // MODIFICADO - No procesar entrada continua si está pausado
        if (isPaused) {
            return;
        }
        
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

    public void update() {
        if (!gameOver && !isPaused) {
            handleContinuousInput();
            updateGameObjects();
            checkCollisions();
        }
    }

    private void updateGameObjects() {
        // Actualizar balas del jugador
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.move();
            if (bullet.getY() < 0) {
                bulletIter.remove();
            }
        }

        // Actualizar enemigos
        for (Enemy enemy : enemies) {
            enemy.move();
            double shootChance = 0.0005 * (1 + level * 0.5);
            if (Math.random() < shootChance) {
                enemyBullets.add(new EnemyBullet(enemy.getX() + 15, enemy.getY() + 30));
            }
        }

        // Actualizar balas enemigas
        Iterator<EnemyBullet> enemyBulletIter = enemyBullets.iterator();
        while (enemyBulletIter.hasNext()) {
            EnemyBullet enemyBullet = enemyBulletIter.next();
            enemyBullet.move();
            if (enemyBullet.getY() > BOARD_HEIGHT) {
                enemyBulletIter.remove();
            }
        }
    }

    private void checkCollisions() {
        // Colisiones balas del jugador con enemigos
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bulletIter.remove();
                    enemyIter.remove();

                    int levelBonus = (level - 1) * 50;
                    score += enemy.getType().getBaseScore() + levelBonus;
                    break;
                }
            }
        }

        // Colisiones balas enemigas con jugador
        Iterator<EnemyBullet> enemyBulletIter = enemyBullets.iterator();
        while (enemyBulletIter.hasNext()) {
            EnemyBullet enemyBullet = enemyBulletIter.next();
            if (enemyBullet.getBounds().intersects(player.getBounds())) {
                enemyBulletIter.remove();
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                    checkForHighScore(); // NUEVO - verificar high score
                }
                break;
            }
        }

        // Avanzar al siguiente nivel
        if (enemies.isEmpty()) {
            if (level < 10) {
                level++;
                score += level * 1000;
            }
            createEnemies();
        }
    }

    private void checkForHighScore() {
        if (highScoreRepository.isQualifyingScore(score)) {
            // Notificar a la vista que debe mostrar el diálogo
            view.showHighScoreDialog(score, level);
        }
    }

    public void onHighScoreSubmitted(String playerName, int score, int level) {
        boolean success = highScoreRepository.saveHighScore(playerName, score, level);
        
        if (success) {
            view.showHighScoreSuccess();
        } else {
            view.showHighScoreError();
        }
    }

    public void onKeyPressed(int keyCode) {
        pressedKeys.add(keyCode);
        
        if (gameOver) {
            if (keyCode == KeyEvent.VK_R) {
                restartGame();
            } else if (keyCode == KeyEvent.VK_M) {
                onReturnToMenu();
            }
        } else {
            if (keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_ESCAPE) {
                togglePause();
            }
        }
    }

    public void onKeyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
    }

    private void togglePause() {
        isPaused = !isPaused;
    }

    public void onReturnToMenu() {
        returnToMenu = true;
        isPaused = false;
        pressedKeys.clear();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }

    public void resetReturnToMenu() {
        returnToMenu = false;
    }

    // Getters para que la vista acceda a los datos
    public Player getPlayer() {
        return player;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public List<HighScore> getHighScores() {
        return highScoreRepository.getTopHighScores();
    }

    public ArrayList<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
