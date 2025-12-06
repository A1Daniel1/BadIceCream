package domain;

/**
 * Interfaz para entidades que tienen vida y pueden morir o revivir.
 */
public interface Alive {
    /**
     * Verifica si la entidad está viva.
     * 
     * @return true si la entidad está viva.
     */
    boolean isAlive();

    /**
     * Mata a la entidad, cambiando su estado a muerto.
     */
    void die();

    /**
     * Revive a la entidad, restaurando su estado a vivo.
     */
    void revive();
}
