package fr.ul.acl.model;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {
    private Clip clip;
    private boolean muted = false;
    private FloatControl volumeControl;

    public void playBackgroundMusic() {
        try {
            URL url = getClass().getResource("/bgm.wav");

            if (url == null) {
                throw new IllegalStateException("bgm.wav not found!");
            }

            AudioInputStream audio =
                AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(audio);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(
                    FloatControl.Type.MASTER_GAIN
                );
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (UnsupportedAudioFileException |
                 IOException |
                 LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setMuted(boolean muted) {
        this.muted = muted;

        if (volumeControl != null) {
            volumeControl.setValue(
                muted ? volumeControl.getMinimum() : 0.0f
            );
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
