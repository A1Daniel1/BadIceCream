package domain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controlador de entrada del juego para el Jugador 1.
 * Solo escucha WASD y Espacio (ignora completamente las flechas).
 */
public class GameController implements KeyListener {
    private Game game;

    public GameController(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // IGNORAR COMPLETAMENTE las teclas de flecha
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || 
            keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            return; // No procesar estas teclas
        }

        switch (keyCode) {
            // Solo WASD para movimiento del jugador 1
            case KeyEvent.VK_W:
                game.movePlayer(Direction.UP);
                break;
            case KeyEvent.VK_S:
                game.movePlayer(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                game.movePlayer(Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                game.movePlayer(Direction.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                Player player = game.getPlayer();
                if (player != null) {
                    Position targetPos = player.getPosition().add(player.getDirection());
                    if (game.getLevel().isIceBlock(targetPos)) {
                        game.destroyIceBlock();
                    } else {
                        game.createIceBlock();
                    }
                }
                break;
            case KeyEvent.VK_P:
            case KeyEvent.VK_ESCAPE:
                if (game.getState() == GameState.PLAYING) {
                    game.pause();
                } else if (game.getState() == GameState.PAUSED) {
                    game.resume();
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