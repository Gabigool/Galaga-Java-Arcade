package main.java.com.galaga.persistence.dao;

import main.java.com.galaga.model.HighScore;
import main.java.com.galaga.persistence.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreDAO {
    
    public HighScoreDAO() {
        initializeDatabase();
    }
    
    /**
     * Inicializa la base de datos y crea la tabla si no existe
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL)) {
            if (conn != null) {
                System.out.println("✅ Base de datos SQLite conectada exitosamente");
                createTable(conn);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * Crea la tabla de high scores si no existe
     */
    private void createTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(DatabaseConfig.CREATE_TABLE_SQL);
            System.out.println("✅ Tabla 'high_scores' verificada/creada exitosamente");
        }
    }
    
    /**
     * Inserta un nuevo high score en la base de datos
     */
    public boolean insert(HighScore highScore) {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(DatabaseConfig.INSERT_SCORE_SQL)) {
            
            // Establecer parámetros de forma segura (evita SQL injection)
            pstmt.setString(1, highScore.getPlayerName());
            pstmt.setInt(2, highScore.getScore());
            pstmt.setInt(3, highScore.getLevelReached());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ High score guardado: " + highScore.getPlayerName() + " - " + highScore.getScore());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar high score: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Obtiene los mejores puntajes ordenados de mayor a menor
     */
    public List<HighScore> getTopScores(int limit) {
        List<HighScore> topScores = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(DatabaseConfig.SELECT_TOP_SCORES_SQL)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HighScore highScore = new HighScore(
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getInt("level_reached"),
                        rs.getString("date_achieved")
                    );
                    topScores.add(highScore);
                }
            }
            
            System.out.println("✅ Obtenidos " + topScores.size() + " high scores");
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener high scores: " + e.getMessage());
        }
        
        return topScores;
    }
    
    /**
     * Verifica si un puntaje califica para el top 10
     */
    public boolean isHighScore(int score) {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Primero verificar si hay menos de 10 puntajes
            ResultSet countRs = stmt.executeQuery(DatabaseConfig.COUNT_SCORES_SQL);
            if (countRs.next() && countRs.getInt(1) < DatabaseConfig.MAX_HIGH_SCORES) {
                return true; // Siempre califica si hay menos de 10
            }
            
            // Si hay 10 o más, verificar si supera el puntaje mínimo del top 10
            try (PreparedStatement pstmt = conn.prepareStatement(DatabaseConfig.MIN_TOP_SCORE_SQL)) {
                pstmt.setInt(1, DatabaseConfig.MAX_HIGH_SCORES);
                ResultSet minRs = pstmt.executeQuery();
                if (minRs.next()) {
                    return score > minRs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar high score: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Elimina puntajes antiguos manteniendo solo el top 10
     */
    public void cleanupOldScores() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(DatabaseConfig.CLEANUP_OLD_SCORES_SQL)) {
            
            pstmt.setInt(1, DatabaseConfig.MAX_HIGH_SCORES);
            int deletedRows = pstmt.executeUpdate();
            
            if (deletedRows > 0) {
                System.out.println("🧹 Eliminados " + deletedRows + " puntajes antiguos");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al limpiar puntajes antiguos: " + e.getMessage());
        }
    }
}
