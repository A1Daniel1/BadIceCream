package presentation;

import javax.swing.*;
import domain.Enemy;
import domain.Fruit;
import domain.Game;
import domain.GameController;
import domain.GameState;
import domain.IceCreamFlavour;
import domain.Level;
import domain.Player;
import domain.Position;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Panel de menú principal
 */
class MenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton startButton;
    private JComboBox<String> modeComboBox;
    private JComboBox<IceCreamFlavour> flavorComboBox;
    private GameFrame gameFrame;
    
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(100, 180, 255));
        
        // Título
        JLabel titleLabel = new JLabel("Bad Dopo-Cream");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("DOPO 2025-2");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.WHITE);
        
        // Selección de modalidad
        JLabel modeLabel = new JLabel("Modalidad de Juego:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel.setForeground(Color.WHITE);
        
        String[] modes = {"Player vs Player", "Player vs Machine", "Machine vs Machine"};
        modeComboBox = new JComboBox<>(modes);
        modeComboBox.setMaximumSize(new Dimension(300, 40));
        modeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Selección de sabor
        JLabel flavorLabel = new JLabel("Sabor de Helado:");
        flavorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        flavorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flavorLabel.setForeground(Color.WHITE);
        
        flavorComboBox = new JComboBox<>(IceCreamFlavour.values());
        flavorComboBox.setMaximumSize(new Dimension(300, 40));
        flavorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botón de inicio
        startButton = new JButton("Iniciar Juego");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(34, 197, 94));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startGame());
        
        // Instrucciones
        JTextArea instructions = new JTextArea(
            "Controles:\n" +
            "- Flechas o WASD: Mover\n" +
            "- ESPACIO: Crear bloque de hielo\n" +
            "- SHIFT + ESPACIO: Destruir bloque\n" +
            "- P o ESC: Pausar"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(255, 255, 255, 200));
        instructions.setFont(new Font("Arial", Font.PLAIN, 14));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Agregar componentes con espaciado
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(subtitleLabel);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(modeLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(modeComboBox);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(flavorLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(flavorComboBox);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(instructions);
        add(Box.createVerticalGlue());
    }
    
    private void startGame() {
        IceCreamFlavour flavor = (IceCreamFlavour) flavorComboBox.getSelectedItem();
        if (flavor != null) {
            gameFrame.startGame(flavor);
        }
    }
}

/**
 * Panel del HUD (Head-Up Display)
 */
class HUDPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel fruitsLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton menuButton;
    private Game game;
    
    public HUDPanel(Game game, GameFrame gameFrame) {
        this.game = game;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 60));
        
        fruitsLabel = new JLabel("Frutas: 0/0");
        fruitsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        scoreLabel = new JLabel("Puntos: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        timeLabel = new JLabel("Tiempo: 3:00");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        pauseButton = new JButton("||");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 16));
        pauseButton.addActionListener(e -> togglePause());
        
        resetButton = new JButton("R");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(e -> game.reset());
        
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton.addActionListener(e -> gameFrame.showMenu());
        
        add(fruitsLabel);
        add(scoreLabel);
        add(timeLabel);
        add(pauseButton);
        add(resetButton);
        add(menuButton);
    }
    
    public void update() {
        Level level = game.getLevel();
        Player player = game.getPlayer();
        
        if (level != null && player != null) {
            int collected = (int) level.getFruits().stream().filter(Fruit::isCollected).count();
            int total = level.getFruits().size();
            fruitsLabel.setText("Frutas: " + collected + "/" + total);
            
            scoreLabel.setText("Puntos: " + player.getScore());
            
            int time = game.getTimeRemaining();
            int mins = time / 60;
            int secs = time % 60;
            timeLabel.setText(String.format("Tiempo: %d:%02d", mins, secs));
            
            pauseButton.setText(game.getState() == GameState.PAUSED ? ">" : "||");
        }
    }
    
    private void togglePause() {
        if (game.getState() == GameState.PLAYING) {
            game.pause();
        } else if (game.getState() == GameState.PAUSED) {
            game.resume();
        }
    }
}

/**
 * Panel del tablero de juego
 */
class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Game game;
    private static final int CELL_SIZE = 40;
    
    public GameBoardPanel(Game game) {
        this.game = game;
        setBackground(new Color(200, 230, 255));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Level level = game.getLevel();
        if (level == null) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = level.getWidth();
        int height = level.getHeight();
        
        // Calcular offset para centrar el tablero
        int offsetX = (getWidth() - width * CELL_SIZE) / 2;
        int offsetY = (getHeight() - height * CELL_SIZE) / 2;
        
        // Dibujar celdas
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);
                int drawX = offsetX + x * CELL_SIZE;
                int drawY = offsetY + y * CELL_SIZE;
                
                // Color de fondo
                if (level.isWall(pos)) {
                    g2d.setColor(new Color(70, 70, 70));
                } else if (level.isIceBlock(pos)) {
                    g2d.setColor(new Color(173, 216, 230));
                } else {
                    g2d.setColor(new Color(240, 248, 255));
                }
                g2d.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                
                // Borde
                g2d.setColor(Color.GRAY);
                g2d.drawRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                
                // Contenido de la celda
                drawCellContent(g2d, pos, drawX, drawY);
            }
        }
        
        // Dibujar overlay de estado
        drawStateOverlay(g2d);
    }
    
    private void drawCellContent(Graphics2D g2d, Position pos, int x, int y) {
        Level level = game.getLevel();
        Player player = game.getPlayer();
        
        // Dibujar jugador
        if (player != null && player.getPosition() != null && player.getPosition().equals(pos)) {
            g2d.setColor(Color.decode(player.getFlavor().getColor()));
            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("P", x + 13, y + 27);
        }
        
        // Dibujar enemigos
        if (level.getEnemies() != null) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy != null && enemy.getPosition() != null && 
                    enemy.getPosition().equals(pos) && enemy.isAlive()) {
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    g2d.drawString(enemy.getSymbol(), x + 13, y + 27);
                }
            }
        }
        
        // Dibujar frutas
        if (level.getFruits() != null) {
            for (Fruit fruit : level.getFruits()) {
                if (fruit != null && fruit.getPosition() != null && 
                    fruit.getPosition().equals(pos) && !fruit.isCollected()) {
                    Color fruitColor;
                    switch (fruit.getFruitType()) {
                        case GRAPE:
                            fruitColor = new Color(138, 43, 226);
                            break;
                        case BANANA:
                            fruitColor = new Color(255, 255, 0);
                            break;
                        default:
                            fruitColor = Color.GREEN;
                    }
                    g2d.setColor(fruitColor);
                    g2d.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.drawString(fruit.getSymbol(), x + 15, y + 27);
                }
            }
        }
    }
    
    private void drawStateOverlay(Graphics2D g2d) {
        GameState state = game.getState();
        String message = null;
        Color bgColor = null;
        
        switch (state) {
            case PAUSED:
                message = "PAUSADO";
                bgColor = new Color(0, 0, 0, 180);
                break;
            case GAME_OVER:
                message = "GAME OVER";
                bgColor = new Color(139, 0, 0, 200);
                break;
            case VICTORY:
                message = "VICTORIA!";
                bgColor = new Color(0, 128, 0, 200);
                break;
            default:
                break;
        }
        
        if (message != null) {
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(message);
            g2d.drawString(message, (getWidth() - msgWidth) / 2, getHeight() / 2);
            
            if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
                Player player = game.getPlayer();
                if (player != null) {
                    String scoreMsg = "Puntos: " + player.getScore();
                    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                    fm = g2d.getFontMetrics();
                    int scoreWidth = fm.stringWidth(scoreMsg);
                    g2d.drawString(scoreMsg, (getWidth() - scoreWidth) / 2, getHeight() / 2 + 50);
                }
            }
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (game.getLevel() != null) {
            int width = game.getLevel().getWidth() * CELL_SIZE;
            int height = game.getLevel().getHeight() * CELL_SIZE;
            return new Dimension(width + 100, height + 100);
        }
        return new Dimension(800, 600);
    }
}

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

/**
 * Clase principal con el método main
 */
public class BadIcreamGUI {
    public static void main(String[] args) {
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}