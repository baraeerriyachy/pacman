package fr.ul.acl.model;

import java.awt.event.KeyEvent;

import fr.ul.acl.engine.Cmd;
import fr.ul.acl.engine.GameController;

public class HachimiController implements GameController {

    private Cmd commandeEnCours;

    public enum KeyboardLayout {
        AZERTY, QWERTY
    }

    private KeyboardLayout keyboardLayout = KeyboardLayout.QWERTY;
    private MusicPlayer musicPlayer;

    public HachimiController() {
        this(null);
    }

    public HachimiController(MusicPlayer musicPlayer) {
        this.commandeEnCours = Cmd.IDLE;
        this.musicPlayer = musicPlayer;
    }

    public Cmd getCommand() {
        return this.commandeEnCours;
    }

    public void setKeyboardLayout(KeyboardLayout layout) {
        this.keyboardLayout = layout;
    }

    public KeyboardLayout getKeyboardLayout() {
        return keyboardLayout;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            // Arrow keys always work
            case KeyEvent.VK_LEFT:
                commandeEnCours = Cmd.LEFT;
                break;

            case KeyEvent.VK_RIGHT:
                commandeEnCours = Cmd.RIGHT;
                break;

            case KeyEvent.VK_UP:
                commandeEnCours = Cmd.UP;
                break;

            case KeyEvent.VK_DOWN:
                commandeEnCours = Cmd.DOWN;
                break;

            // AZERTY: ZQSD
            case KeyEvent.VK_Z:
                if (keyboardLayout == KeyboardLayout.AZERTY)
                    commandeEnCours = Cmd.UP;
                break;

            case KeyEvent.VK_Q:
                if (keyboardLayout == KeyboardLayout.AZERTY)
                    commandeEnCours = Cmd.LEFT;
                break;

            // QWERTY: WASD
            case KeyEvent.VK_W:
                if (keyboardLayout == KeyboardLayout.QWERTY)
                    commandeEnCours = Cmd.UP;
                break;

            case KeyEvent.VK_A:
                if (keyboardLayout == KeyboardLayout.QWERTY)
                    commandeEnCours = Cmd.LEFT;
                break;

            // Shared keys in both layouts
            case KeyEvent.VK_S:
                commandeEnCours = Cmd.DOWN;
                break;

            case KeyEvent.VK_D:
                commandeEnCours = Cmd.RIGHT;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.commandeEnCours = Cmd.IDLE;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to do
    }
}