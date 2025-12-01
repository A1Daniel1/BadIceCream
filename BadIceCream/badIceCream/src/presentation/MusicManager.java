package presentation;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Gestor de música del juego
 */
public class MusicManager {
    private static Clip backgroundMusic;
    private static boolean isMuted = false;
    private static float volume = 0.8f; // Volumen por defecto (0.0 a 1.0)
    
    /**
     * Carga y reproduce la música de fondo en loop
     */
    public static void playBackgroundMusic(String filename) {
        try {
            // Detener música anterior si existe
            stopBackgroundMusic();
            
            // Intentar cargar desde el sistema de archivos
            File audioFile = new File("src/resources/music/" + filename);
            AudioInputStream audioStream;
            
            if (audioFile.exists()) {
                audioStream = AudioSystem.getAudioInputStream(audioFile);
            } else {
                // Intentar cargar desde recursos
                URL url = MusicManager.class.getResource("/resources/music/" + filename);
                if (url == null) {
                    System.err.println("No se encontró el archivo de música: " + filename);
                    return;
                }
                audioStream = AudioSystem.getAudioInputStream(url);
            }
            
            // Crear el clip de audio
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            // Configurar volumen
            setVolume(volume);
            
            // Reproducir en loop infinito
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            
            System.out.println("Música cargada y reproduciendo: " + filename);
            
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Formato de audio no soportado: " + filename);
            System.err.println("Formatos soportados: WAV, AIFF, AU");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de música: " + filename);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Línea de audio no disponible");
            e.printStackTrace();
        }
    }
    
    /**
     * Detiene la música de fondo
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
    
    /**
     * Pausa la música
     */
    public static void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    /**
     * Reanuda la música
     */
    public static void resumeMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning() && !isMuted) {
            backgroundMusic.start();
        }
    }
    
    /**
     * Alterna entre silenciar y activar sonido
     */
    public static void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }
    
    /**
     * Configura el volumen de la música
     * @param vol Volumen entre 0.0 (silencio) y 1.0 (máximo)
     */
    public static void setVolume(float vol) {
        volume = Math.max(0.0f, Math.min(1.0f, vol));
        
        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            try {
                FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                
                // Convertir volumen de 0-1 a decibelios
                float min = volumeControl.getMinimum();
                float max = volumeControl.getMaximum();
                float gain = min + (max - min) * volume;
                
                volumeControl.setValue(gain);
            } catch (IllegalArgumentException e) {
                System.err.println("No se pudo ajustar el volumen");
            }
        }
    }
    
    /**
     * Obtiene el volumen actual
     */
    public static float getVolume() {
        return volume;
    }
    
    /**
     * Verifica si la música está silenciada
     */
    public static boolean isMuted() {
        return isMuted;
    }
    
    /**
     * Verifica si la música está reproduciendo
     */
    public static boolean isPlaying() {
        return backgroundMusic != null && backgroundMusic.isRunning();
    }
}