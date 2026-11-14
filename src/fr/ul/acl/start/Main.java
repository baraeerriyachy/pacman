
package fr.ul.acl.start;

import javax.swing.SwingUtilities;

import fr.ul.acl.engine.GameEngineGraphical;
import fr.ul.acl.model.HachimiController;
import fr.ul.acl.model.MusicPlayer;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MusicPlayer music = new MusicPlayer();
            music.playBackgroundMusic();

            HachimiController controller =
                    new HachimiController(music);

            GameEngineGraphical engine =
                    new GameEngineGraphical(
                        "helpFileHachimi.txt",
                        controller
                    );

            engine.run();
        });
    }
}
