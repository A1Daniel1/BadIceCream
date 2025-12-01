package domain;

/**
 * Jugador (Helado)
 */
public class Player extends Unit implements IceInteractor {
    private IceCreamFlavour flavor;
    private int score;

    public Player(Position position, IceCreamFlavour flavor) {
        super(position);
        this.flavor = flavor;
        this.score = 0;
        // Regenerar el ID después de asignar el flavor
        this.id = generateId();
    }

    public IceCreamFlavour getFlavor() {
        return flavor;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    @Override
    public void createIceBlock(Level level) {
        if (position == null || direction == null || level == null)
            return;

        Position currentPos = position.add(direction);
        while (level.canPlaceIceBlock(currentPos) && !level.hasEnemyAt(currentPos)) {
            level.addIceBlock(new IceBlock(currentPos));
            currentPos = currentPos.add(direction);
        }
    }

    @Override
    public void destroyIceBlock(Level level) {
        if (position == null || direction == null || level == null)
            return;

        Position currentPos = position.add(direction);

        // Destruir en cadena (efecto dominó)
        while (level.isIceBlock(currentPos)) {
            IceBlock block = level.getIceBlockAt(currentPos);
            if (block != null) {
                block.destroy();
            }
            currentPos = currentPos.add(direction);
        }
    }

    @Override
    public boolean canBreakIce() {
        return true;
    }

    @Override
    public boolean canMoveTo(Position position, Level level) {
        return level != null && level.canMoveTo(position);
    }

    @Override
    protected String generateId() {
        if (flavor == null) {
            return "PLAYER_TEMP_" + System.nanoTime();
        }
        return "PLAYER_" + flavor.name();
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}