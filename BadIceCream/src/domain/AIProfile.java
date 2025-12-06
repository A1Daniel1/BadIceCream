package domain;

/**
 * Enumeración que define los perfiles de comportamiento de la IA.
 */
public enum AIProfile {
    /** Prioriza recolectar frutas sobre todo lo demás */
    HUNGRY("Hungry", "Busca frutas agresivamente"),
    
    /** Prioriza evitar enemigos y peligros */
    FEARFUL("Fearful", "Evita enemigos y peligros"),
    
    /** Balancea recolección y supervivencia de manera óptima */
    EXPERT("Expert", "Estrategia experta balanceada");

    private final String name;
    private final String description;

    AIProfile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}