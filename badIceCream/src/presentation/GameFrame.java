package presentation;

import domain.Game;
import domain.Position;
import domain.Level;
import domain.GameController;
import domain.IceCreamFlavour;
import domain.GameState;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Frame principal del juego
 */
class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Game game;
    private MenuPanel menuPanel;
    private JPanel gamePanel;
    private HUDPanel hudPanel;
    private GameBoardPanel boardPanel;
    private GameController controller;
    private Timer gameTimer;
    
    public GameFrame() {
        setTitle("Bad Dopo-Cream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        
        game = new Game();
        controller = new GameController(game);
        
        // Mostrar menú inicial
        showMenu();
    }
    
    public void showMenu() {
        getContentPane().removeAll();
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        revalidate();
        repaint();
        
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }
    
    public void startGame(IceCreamFlavour flavor) {
        try {
            game.startGame(1, flavor);
            
            getContentPane().removeAll();
            
            // Panel de juego
            gamePanel = new JPanel(new BorderLayout());
            
            // HUD
            hudPanel = new HUDPanel(game, this);
            gamePanel.add(hudPanel, BorderLayout.NORTH);
            
            // Tablero
            boardPanel = new GameBoardPanel(game);
            gamePanel.add(boardPanel, BorderLayout.CENTER);
            
            add(gamePanel);
            
            // Remover listeners previos
            for (KeyListener kl : getKeyListeners()) {
                removeKeyListener(kl);
            }
            
            // Agregar listener de teclado
            addKeyListener(controller);
            setFocusable(true);
            requestFocus();
            
            revalidate();
            repaint();
            
            // Iniciar game loop
            startGameLoop();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar el juego: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void startGameLoop() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (game.getState() == GameState.PLAYING) {
                        Game.update();
                        SwingUtilities.invokeLater(() -> {
                            if (hudPanel != null) hudPanel.update();
                            if (boardPanel != null) boardPanel.repaint();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 500); // Actualizar cada 500ms
    }
}
