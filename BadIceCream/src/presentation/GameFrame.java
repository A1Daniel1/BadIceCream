package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Game game;
    private GameController controller;
    private GameController2 controller2; // Para jugador 2 en PvsP
    private MenuPanel menuPanel;
    private JPanel gamePanel;
    private HUDPanel hudPanel;
    private GameBoardPanel boardPanel;
    
    private Timer gameTimer;
    private Timer secondTimer;
    private boolean gameEnded;

    public GameFrame() {
        game = new Game();
        controller = new GameController(game);
        controller2 = new GameController2(game);
        
        setTitle("Bad Dopo-Cream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initMenu();
        showMenu();
        
        MusicManager.playBackgroundMusic("background.wav");
    }
    
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        
        JMenuItem newItem = new JMenuItem("Nuevo");
        JMenuItem openItem = new JMenuItem("Abrir");
        JMenuItem saveItem = new JMenuItem("Guardar");
        JMenuItem importItem = new JMenuItem("Importar");
        JMenuItem exportItem = new JMenuItem("Exportar");
        JMenuItem exitItem = new JMenuItem("Salir");
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        newItem.addActionListener(e -> optionNew());
        openItem.addActionListener(e -> optionOpen());
        saveItem.addActionListener(e -> optionSave());
        importItem.addActionListener(e -> optionImport());
        exportItem.addActionListener(e -> optionExport());
        exitItem.addActionListener(e -> System.exit(0));
    }
    
    private void optionNew() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "¿Desea iniciar un nuevo juego? Se perderán los cambios no guardados.",
            "Nuevo Juego",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            showMenu();
        }
    }
    
    private void optionOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir Partida");
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos de Partida (*.dat)", "dat");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Game loadedGame = Game.open(file);
                this.game = loadedGame;
                startLoadedGame(loadedGame);
                JOptionPane.showMessageDialog(this, "Partida cargada exitosamente", 
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (BadIceCreamException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void optionSave() {
        if (game.getState() == GameState.MENU) {
            JOptionPane.showMessageDialog(this, "No hay partida activa para guardar",
                                         "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Partida");
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos de Partida (*.dat)", "dat");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            if (!file.getName().toLowerCase().endsWith(".dat")) {
                file = new File(file.getAbsolutePath() + ".dat");
            }
            
            try {
                game.save(file);
                JOptionPane.showMessageDialog(this, "Partida guardada exitosamente",
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (BadIceCreamException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void optionImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Importar Nivel");
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Game importedGame = Game.importLevel(file);
                this.game = importedGame;
                startLoadedGame(importedGame);
                JOptionPane.showMessageDialog(this, "Nivel importado exitosamente",
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (BadIceCreamException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void optionExport() {
        if (game.getState() == GameState.MENU) {
            JOptionPane.showMessageDialog(this, "No hay nivel activo para exportar",
                                         "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Nivel");
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            
            try {
                game.export(file);
                JOptionPane.showMessageDialog(this, "Nivel exportado exitosamente",
                                             "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (BadIceCreamException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void showMenu() {
        stopTimers();
        getContentPane().removeAll();
        
        menuPanel = new MenuPanel(this);
        getContentPane().add(menuPanel);
        
        revalidate();
        repaint();
    }
    
    /**
     * Inicia el juego (modo Player simple) - Para compatibilidad con código anterior
     */
    public void startGame(IceCreamFlavour flavor) {
        game.startGame(1, flavor);
        startGameLoop();
    }
    
    /**
     * Inicia el juego con modalidad específica
     */
    public void startGame(int levelNumber, IceCreamFlavour flavor, GameMode mode,
                         AIProfile profile1, AIProfile profile2) {
        game.startGame(levelNumber, flavor, mode, profile1, profile2);
        startGameLoop();
    }
    
    public void startLoadedGame(Game loadedGame) {
        this.game = loadedGame;
        this.controller = new GameController(game);
        this.controller2 = new GameController2(game);
        startGameLoop();
    }
    
    /**
     * Obtiene la instancia del juego
     */
    public Game getGame() {
        return game;
    }
    
    /**
     * Inicia el bucle de juego (público para ser llamado externamente)
     */
    public void startGameLoop() {
        stopTimers();
        gameEnded = false;
        
        getContentPane().removeAll();
        
        gamePanel = new JPanel(new BorderLayout());
        hudPanel = new HUDPanel(game, this);
        boardPanel = new GameBoardPanel(game);
        
        gamePanel.add(hudPanel, BorderLayout.NORTH);
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        
        getContentPane().add(gamePanel);
        
        // Limpiar todos los KeyListeners previos
        for (KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }
        
        // Configurar controles según modalidad
        if (game.getGameMode() == GameMode.PLAYER_VS_PLAYER) {
            // PvsP: ambos controladores activos
            addKeyListener(controller);
            addKeyListener(controller2);
        } else if (game.getGameMode() == GameMode.PLAYER || 
                   game.getGameMode() == GameMode.PLAYER_VS_MACHINE) {
            // Player o PvsM: solo controlador 1
            addKeyListener(controller);
        }
        // En MvsM no se necesitan controles
        
        setFocusable(true);
        requestFocusInWindow();
        
        revalidate();
        repaint();
        
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.getState() == GameState.PLAYING) {
                    Game.update();
                    hudPanel.update();
                    boardPanel.repaint();
                    
                    if (game.getState() == GameState.GAME_OVER || 
                        game.getState() == GameState.VICTORY) {
                        if (!gameEnded) {
                            gameEnded = true;
                            scheduleReturnToMenu();
                        }
                    }
                }
            }
        }, 0, 100);
    }
    
    private void scheduleReturnToMenu() {
        Timer returnTimer = new Timer();
        returnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> showMenu());
            }
        }, 3000);
    }
    
    private void stopTimers() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
        if (secondTimer != null) {
            secondTimer.cancel();
            secondTimer = null;
        }
    }
    
    @Override
    public void dispose() {
        stopTimers();
        MusicManager.stopBackgroundMusic();
        super.dispose();
    }
}