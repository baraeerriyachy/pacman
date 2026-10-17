package fr.ul.acl.start;
import java.io.IOException;

import fr.ul.acl.model.HachimiPainter;
import fr.ul.acl.engine.GameEngineGraphical;
import fr.ul.acl.model.HachimiController;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.MusicPlayer;

/**
 * lancement du moteur avec le jeu
 */
public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		MusicPlayer music = new MusicPlayer();
		music.playBackgroundMusic();

		// creation du jeu particulier et de son afficheur
		HachimiGame game = new HachimiGame("helpFileHachimi.txt");
		HachimiPainter painter = new HachimiPainter(game);
		HachimiController controller = new HachimiController(music);

		// classe qui lance le moteur de jeu generique
		GameEngineGraphical engine = new GameEngineGraphical(game, painter, controller);
		engine.run();
	}

}
