package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona las oleadas de frutas en el nivel
 */
public class FruitWaveManager {
    private int currentWave;
    private int totalWaves;
    private FruitType currentFruitType;
    private List<Position> fruitPositions;
    private boolean waveCompleted;
    
    public FruitWaveManager(int totalWaves) {
        this.currentWave = 0;
        this.totalWaves = totalWaves;
        this.waveCompleted = false;
        this.fruitPositions = new ArrayList<>();
    }
    
    /**
     * Inicia una nueva oleada con un tipo de fruta específico
     */
    public void startWave(int waveNumber, FruitType fruitType, List<Position> positions) {
        this.currentWave = waveNumber;
        this.currentFruitType = fruitType;
        this.fruitPositions = new ArrayList<>(positions);
        this.waveCompleted = false;
    }
    
    /**
     * Genera las frutas para la oleada actual
     */
    public List<Fruit> generateWaveFruits() {
        List<Fruit> fruits = new ArrayList<>();
        
        for (Position pos : fruitPositions) {
            Fruit fruit = createFruit(currentFruitType, pos);
            if (fruit != null) {
                fruits.add(fruit);
            }
        }
        
        return fruits;
    }
    
    private Fruit createFruit(FruitType type, Position position) {
        switch (type) {
            case GRAPE:
                return new Grape(position);
            case BANANA:
                return new Banana(position);
            case PINEAPPLE:
                return new Pineapple(position);
            case CHERRY:
                return new Cherry(position);
            case CACTUS:
                return new Cactus(position);
            default:
                return new Grape(position);
        }
    }
    
    /**
     * Verifica si todas las frutas de la oleada actual fueron recolectadas
     */
    public boolean isWaveCompleted(List<Fruit> levelFruits) {
        if (levelFruits == null || levelFruits.isEmpty()) {
            waveCompleted = true;
            return true;
        }
        
        // Verificar si todas las frutas de la oleada actual están recolectadas
        boolean allCollected = levelFruits.stream().allMatch(Fruit::isCollected);
        
        if (allCollected && !waveCompleted) {
            waveCompleted = true;
        }
        
        return allCollected;
    }
    
    /**
     * Avanza a la siguiente oleada
     */
    public boolean nextWave() {
        if (currentWave < totalWaves) {
            currentWave++;
            waveCompleted = false;
            return true;
        }
        return false;
    }
    
    public boolean hasNextWave() {
        return currentWave < totalWaves;
    }
    
    public int getCurrentWave() {
        return currentWave;
    }
    
    public int getTotalWaves() {
        return totalWaves;
    }
    
    public FruitType getCurrentFruitType() {
        return currentFruitType;
    }
    
    public boolean isAllWavesCompleted() {
        // CORRECCIÓN: La oleada actual debe haber completado Y no debe haber más oleadas
        return currentWave >= totalWaves && waveCompleted;
    }
}