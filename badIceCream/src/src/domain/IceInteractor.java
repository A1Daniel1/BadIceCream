package domain;

/**
 * Interface para entidades que pueden interactuar con bloques de hielo
 */
public interface IceInteractor {
    void createIceBlock(Level level);
    void destroyIceBlock(Level level);
    boolean canBreakIce();
}