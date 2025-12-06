package presentation;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Gestor de música del juego.
 * Controla la reproducción de música de fondo, volumen y estado de silencio.
 */
public class MusicManager {
    private static Clip backgroundMusic;
    private static boolean isMuted = false;
    private static float volume = 0.8f; // Volumen por defecto (0.0 a 1.0)
    private static boolean musicAvailable = false;
    /**
     * Carga y reproduce la música de fondo en bucle.
     * 
     * @param filename Nombre del archivo de audio a reproducir.
     */
    public static void playBackgroundMusic(String filename) {
        try {
            stopBackgroundMusic();
            
            File audioFile = new File("src/resources/music/" + filename);
            AudioInputStream audioStream = null;
            
            if (audioFile.exists()) {
                audioStream = AudioSystem.getAudioInputStream(audioFile);
            } else {
                URL url = MusicManager.class.getResource("/resources/music/" + filename);
                if (url != null) {
                    audioStream = AudioSystem.getAudioInputStream(url);
                }
            }
            
            if (audioStream == null) {
                System.out.println("Música no disponible - El juego continuará sin sonido");
                musicAvailable = false;
                return;
            }
            
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            setVolume(volume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            musicAvailable = true;
            System.out.println("Música cargada: " + filename);
            
        } catch (Exception e) {
            System.out.println("No se pudo cargar la música - El juego continuará sin sonido");
            musicAvailable = false;
        }
    }

    /**
     * Detiene y libera los recursos de la música de fondo actual.
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    /**
     * Pausa la reproducción de la música.
     */
    public static void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    /**
     * Reanuda la reproducción de la música si no está silenciada.
     */
    public static void resumeMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning() && !isMuted && musicAvailable) {
            backgroundMusic.start();
        }
    }

    /**
     * Alterna el estado de silencio (mute) de la música.
     */
    public static void toggleMute() {
    	if (!musicAvailable) return;
        isMuted = !isMuted;
        if (isMuted) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    /**
     * Configura el volumen de la música.
     * 
     * @param vol Volumen entre 0.0 (silencio) y 1.0 (máximo).
     */
    public static void setVolume(float vol) {
        if (!musicAvailable) return;
        volume = Math.max(0.0f, Math.min(1.0f, vol));
        
        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            try {
                FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                float min = volumeControl.getMinimum();
                float max = volumeControl.getMaximum();
                float gain = min + (max - min) * volume;
                volumeControl.setValue(gain);
            } catch (Exception e) {
                // Ignorar si no se puede ajustar el volumen
            }
        }
    }

    /**
     * Obtiene el nivel de volumen actual.
     * 
     * @return El volumen actual (0.0 a 1.0).
     */
    public static float getVolume() {
        return volume;
    }

    /**
     * Verifica si la música está silenciada.
     * 
     * @return true si está silenciada, false en caso contrario.
     */
    public static boolean isMuted() {
        return isMuted;
    }

    /**
     * Verifica si la música se está reproduciendo actualmente.
     * 
     * @return true si se está reproduciendo, false en caso contrario.
     */
    public static boolean isPlaying() {
        return musicAvailable && backgroundMusic != null && backgroundMusic.isRunning();
    }
    
    /**
     * Verifica si la música está disponible actualmente.
     * 
     * @return true si está disponible, false en caso contrario.
     */
    public static boolean isMusicAvailable() {
        return musicAvailable;
    }
}
