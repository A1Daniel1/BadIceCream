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
 * Clase principal que contiene el punto de entrada de la aplicación (main).
 * Configura el Look and Feel e inicia la interfaz gráfica.
 */
public class BadIcreamGUI {
    /**
     * Método principal de la aplicación.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
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