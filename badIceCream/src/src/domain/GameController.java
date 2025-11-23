package domain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {
    private Game game;
    
    public GameController(Game game) {
        this.game = game;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch(keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                game.movePlayer(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                game.movePlayer(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                game.movePlayer(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                game.movePlayer(Direction.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                if (e.isShiftDown()) {
                    game.destroyIceBlock();
                } else {
                    game.createIceBlock();
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
        // No se necesita implementación
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita implementación
    }
}