package main.java.com.galaga.persistence.repository;

import main.java.com.galaga.model.HighScore;
import main.java.com.galaga.persistence.dao.HighScoreDAO;
import java.util.List;

public class HighScoreRepository {
    private final HighScoreDAO highScoreDAO;
    
    public HighScoreRepository() {
        this.highScoreDAO = new HighScoreDAO();
    }
    
    /**
     * Guarda un nuevo high score
     */
    public boolean saveHighScore(String playerName, int score, int levelReached) {
        HighScore highScore = new HighScore(playerName, score, levelReached);
        boolean success = highScoreDAO.insert(highScore);
        
        if (success) {
            // Limpiar puntajes antiguos para mantener solo el top 10
            highScoreDAO.cleanupOldScores();
        }
        
        return success;
    }
    
    /**
     * Obtiene los mejores puntajes
     */
    public List<HighScore> getTopHighScores() {
        return highScoreDAO.getTopScores(10);
    }
    
    /**
     * Verifica si un puntaje califica como high score
     */
    public boolean isQualifyingScore(int score) {
        return highScoreDAO.isHighScore(score);
    }
}
