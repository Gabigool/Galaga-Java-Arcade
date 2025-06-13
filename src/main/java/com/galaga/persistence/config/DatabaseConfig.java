package main.java.com.galaga.persistence.config;

public class DatabaseConfig {
    // URL de conexión - crea el archivo galaga_scores.db en la carpeta del proyecto
    public static final String DB_URL = "jdbc:sqlite:galaga_scores.db";
    
    // Configuraciones
    public static final String TABLE_NAME = "high_scores";
    public static final int MAX_HIGH_SCORES = 10;
    
    // SQL para crear la tabla
    public static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS high_scores (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            player_name TEXT NOT NULL,
            score INTEGER NOT NULL,
            level_reached INTEGER NOT NULL,
            date_achieved DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    """;
    
    // SQL queries
    public static final String INSERT_SCORE_SQL = 
        "INSERT INTO high_scores (player_name, score, level_reached) VALUES (?, ?, ?)";
    
    public static final String SELECT_TOP_SCORES_SQL = 
        "SELECT player_name, score, level_reached, date_achieved " +
        "FROM high_scores ORDER BY score DESC LIMIT ?";
    
    public static final String COUNT_SCORES_SQL = 
        "SELECT COUNT(*) FROM high_scores";
    
    public static final String MIN_TOP_SCORE_SQL = 
        "SELECT MIN(score) FROM high_scores WHERE score IN " +
        "(SELECT score FROM high_scores ORDER BY score DESC LIMIT ?)";
    
    public static final String CLEANUP_OLD_SCORES_SQL = 
        "DELETE FROM high_scores WHERE id NOT IN " +
        "(SELECT id FROM high_scores ORDER BY score DESC LIMIT ?)";
}
