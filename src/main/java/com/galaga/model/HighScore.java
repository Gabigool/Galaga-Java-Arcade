package main.java.com.galaga.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HighScore {
    private final String playerName;
    private final int score;
    private final int levelReached;
    private final LocalDateTime dateAchieved;
    
    public HighScore(String playerName, int score, int levelReached, String dateAchieved) {
        this.playerName = playerName;
        this.score = score;
        this.levelReached = levelReached;
        // Convertir string de base de datos a LocalDateTime
        this.dateAchieved = LocalDateTime.parse(dateAchieved.replace(" ", "T"));
    }
    
    // Constructor para crear nuevos high scores
    public HighScore(String playerName, int score, int levelReached) {
        this.playerName = playerName;
        this.score = score;
        this.levelReached = levelReached;
        this.dateAchieved = LocalDateTime.now();
    }
    
    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getLevelReached() { return levelReached; }
    public LocalDateTime getDateAchieved() { return dateAchieved; }
    
    public String getFormattedDate() {
        return dateAchieved.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %d pts (Level %d) - %s", 
                           playerName, score, levelReached, getFormattedDate());
    }
}
