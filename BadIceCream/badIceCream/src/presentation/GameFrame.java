package presentation;

import domain.Game;
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
 * Ventana principal del juego.
 * Gestiona la navegación entre paneles (menú, juego) y el bucle principal de
 * renderizado.
 */
public class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Game game;
    private MenuPanel menuPanel;
    private JPanel gamePanel;
    private HUDPanel hudPanel;
    private GameBoardPanel boardPanel;
    private GameController controller;
    private Timer gameTimer;
    private Timer secondTimer;
    private boolean gameEnded = false;

    /**
     * Constructor de la clase GameFrame.
     * Inicializa la ventana, el juego y muestra el menú principal.
     */
    public GameFrame() {
        setTitle("Bad Dopo-Cream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);

        game = new Game();
        controller = new GameController(game);

        // Iniciar música de fondo al crear el frame
        MusicManager.playBackgroundMusic("background.wav");

        showMenu();
    }

    /**
     * Muestra el panel del menú principal.
     * Detiene los temporizadores del juego si están activos.
     */
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
        if (secondTimer != null) {
            secondTimer.cancel();
            secondTimer = null;
        }

        // Asegurar que la música siga sonando
        if (!MusicManager.isPlaying() && !MusicManager.isMuted()) {
            MusicManager.resumeMusic();
        }

        gameEnded = false;
    }

    /**
     * Inicia una nueva partida con el sabor seleccionado.
     * Configura los paneles de juego y HUD, y arranca el bucle de juego.
     * 
     * @param flavor El sabor de helado seleccionado por el jugador.
     */
    public void startGame(IceCreamFlavour flavor) {
        try {
            game.startGame(1, flavor);
            gameEnded = false;

            getContentPane().removeAll();

            gamePanel = new JPanel(new BorderLayout());

            hudPanel = new HUDPanel(game, this);
            gamePanel.add(hudPanel, BorderLayout.NORTH);

            boardPanel = new GameBoardPanel(game);
            gamePanel.add(boardPanel, BorderLayout.CENTER);

            add(gamePanel);

            for (KeyListener kl : getKeyListeners()) {
                removeKeyListener(kl);
            }

            addKeyListener(controller);
            setFocusable(true);
            requestFocus();

            revalidate();
            repaint();

            startGameLoop();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al iniciar el juego: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicia los temporizadores para el bucle de juego y la actualización del HUD.
     */
    private void startGameLoop() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        if (secondTimer != null) {
            secondTimer.cancel();
        }

        // Timer principal del juego - actualiza lógica a 10 FPS
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    GameState currentState = game.getState();

                    if (currentState == GameState.PLAYING) {
                        Game.update();
                    }

                    // Actualizar el panel del juego
                    SwingUtilities.invokeLater(() -> {
                        if (boardPanel != null)
                            boardPanel.repaint();
                    });

                    // Detectar fin del juego y volver al menú después de 3 segundos
                    if (!gameEnded && (currentState == GameState.VICTORY || currentState == GameState.GAME_OVER)) {
                        gameEnded = true;
                        // Cancelar timers antes de esperar
                        if (gameTimer != null) {
                            gameTimer.cancel();
                        }
                        if (secondTimer != null) {
                            secondTimer.cancel();
                        }

                        // Esperar 3 segundos antes de volver al menú
                        Timer endTimer = new Timer();
                        endTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> showMenu());
                                endTimer.cancel();
                            }
                        }, 3000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100);

        // Timer separado para actualizar el HUD cada 100ms
        secondTimer = new Timer();
        secondTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    SwingUtilities.invokeLater(() -> {
                        if (hudPanel != null)
                            hudPanel.update();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100);
    }

    @Override
    public void dispose() {
        // Detener música al cerrar el juego
        MusicManager.stopBackgroundMusic();
        super.dispose();
    }
}