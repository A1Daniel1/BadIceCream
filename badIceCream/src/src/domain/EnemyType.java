package domain;

public enum EnemyType {
    TROLL("Troll", false, false),
    POT("Maceta", true, false),
    ORANGE_SQUID("Calamar Naranja", true, true);
    
    private final String name;
    private final boolean canChase;
    private final boolean canBreakBlocks;
    
    EnemyType(String name, boolean canChase, boolean canBreakBlocks) {
        this.name = name;
        this.canChase = canChase;
        this.canBreakBlocks = canBreakBlocks;
    }
    
    public String getName() { return name; }
    public boolean canChase() { return canChase; }
    public boolean canBreakBlocks() { return canBreakBlocks; }
}