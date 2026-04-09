import javax.sound.sampled.*;
import java.io.File;
 
public class SoundManager {
    private Clip backgroundMusic;
 
    public void playSound(String soundName) {
        new Thread(() -> {
            try {
                File sFile = findPath(soundName);
                if (sFile != null) {
                    AudioInputStream ai = AudioSystem.getAudioInputStream(sFile);
                    Clip c = AudioSystem.getClip();
                    c.open(ai); c.start();
                }
            } catch (Exception e) {}
        }).start();
    }
 
    public void playMusic(String soundName, boolean loop) {
        try {
            stopMusic();
            File sFile = findPath(soundName);
            if (sFile != null) {
                AudioInputStream ai = AudioSystem.getAudioInputStream(sFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(ai);
                if (loop) backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            }
        } catch (Exception e) {}
    }
 
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
 
    private File findPath(String name) {
        String[] paths = { name, "assets/" + name, "assets/assets/" + name };
        for (String p : paths) {
            File f = new File(p);
            if (f.exists()) return f;
        }
        return null;
    }
}