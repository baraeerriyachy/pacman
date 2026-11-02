package fr.ul.acl.start;

import java.io.IOException;

import javax.swing.JOptionPane;

import fr.ul.acl.engine.GameEngineGraphical;
import fr.ul.acl.model.GameLevel;
import fr.ul.acl.model.HachimiController;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.HachimiPainter;
import fr.ul.acl.model.MusicPlayer;

/** Point d'entree de l'application. */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        GameLevel level = choisirNiveau();
        if (level == null) {
            return;
        }

        MusicPlayer music = new MusicPlayer();
        music.playBackgroundMusic();

        HachimiGame game = new HachimiGame("helpFileHachimi.txt", level);
        HachimiPainter painter = new HachimiPainter(game);
        HachimiController controller = new HachimiController(music);

        GameEngineGraphical engine = new GameEngineGraphical(game, painter, controller);
        engine.run();
    }

    /** Affiche le choix des niveaux avant de lancer la partie. */
    private static GameLevel choisirNiveau() {
        GameLevel[] levels = GameLevel.getAvailableLevels().toArray(new GameLevel[0]);
        Object selected = JOptionPane.showInputDialog(
            null,
            "Choisissez la difficulte :",
            "Selection du niveau - Hachimi",
            JOptionPane.QUESTION_MESSAGE,
            null,
            levels,
            GameLevel.getDefaultLevel()
        );
        return selected instanceof GameLevel ? (GameLevel) selected : null;
    }
}
