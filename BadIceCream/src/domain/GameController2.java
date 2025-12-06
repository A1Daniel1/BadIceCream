package domain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controlador de entrada para el segundo jugador en modo PvsP.
 * Solo usa las teclas de flecha y Enter/NumPad0 (ignora completamente WASD).
 */
public class GameController2 implements KeyListener {
    private Game game;

    public GameController2(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Solo funcionar en modo PvsP
        if (game.getGameMode() != GameMode.PLAYER_VS_PLAYER) {
            return;
        }

        // IGNORAR COMPLETAMENTE las teclas WASD y Espacio
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_A || 
            keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D || 
            keyCode == KeyEvent.VK_SPACE) {
            return; // No procesar estas teclas
        }

        switch (keyCode) {
            // Solo flechas para movimiento del jugador 2
            case KeyEvent.VK_UP:
                game.movePlayer2(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                game.movePlayer2(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                game.movePlayer2(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                game.movePlayer2(Direction.RIGHT);
                break;
            // Enter o NumPad0 para crear/destruir hielo
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_NUMPAD0:
                Player player2 = game.getPlayer2();
                if (player2 != null) {
                    Position targetPos = player2.getPosition().add(player2.getDirection());
                    if (game.getLevel().isIceBlock(targetPos)) {
                        game.destroyIceBlockPlayer2();
                    } else {
                        game.createIceBlockPlayer2();
                    }
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}