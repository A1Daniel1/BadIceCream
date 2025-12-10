package domain;


/**
 * Representa una acción decidida por la IA
 */
public class AIAction {
    private AIActionType type;
    private Direction direction;

    /**
     * Constructor de AIAction.
     * 
     * @param type      Tipo de acción.
     * @param direction Dirección de la acción (puede ser null para WAIT).
     */
    public AIAction(AIActionType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    /**
     * Obtiene el tipo de acción.
     * 
     * @return El tipo de acción.
     */
    public AIActionType getType() {
        return type;
    }

    /**
     * Obtiene la dirección de la acción.
     * 
     * @return La dirección.
     */
    public Direction getDirection() {
        return direction;
    }
}
