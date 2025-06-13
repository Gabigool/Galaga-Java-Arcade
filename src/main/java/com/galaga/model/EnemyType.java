package main.java.com.galaga.model;

public enum EnemyType {
    BOSS(400),    // Galaga (azul)
    RED(160),     // Enemigo rojo  
    BEE(100);     // Abeja (amarilla)
    
    private final int baseScore;
    
    EnemyType(int baseScore) {
        this.baseScore = baseScore;
    }
    
    public int getBaseScore() {
        return baseScore;
    }
}
