package fr.ul.acl.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import java.net.URL;

import fr.ul.acl.engine.GamePainter;
import fr.ul.acl.model.HachimiGame;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 *
 * afficheur graphique pour le game
 * 
 */
public class HachimiPainter implements GamePainter {

	/**
	 * la taille des cases
	 */
	private static final int TAILLE_CASE = 60;
	private HachimiGame game;
	
	private Map<String, Image> images = new HashMap<>();

	private void loadImage(String name, String filename) {
		URL url = getClass().getResource("/" + filename);

		if (url == null) {
			throw new IllegalStateException(filename + " not found!");
		}

		images.put(name, new ImageIcon(url).getImage());
	}

	/**
	 * appelle constructeur parent
	 * 
	 * @param game
	 *            le jeutest a afficher
	 */
	public HachimiPainter(HachimiGame game) {
		this.game = game;

		loadImage("background", "background.jpg");
    	loadImage("hero", "cat.gif");
		loadImage("wall", "wall.png");
		loadImage("sortie", "sortie.png");
	}

	/**
	 * methode  redefinie de Afficheur retourne une image du jeu
	 */
	@Override
	public void draw(BufferedImage im) {
		Graphics2D crayon = (Graphics2D) im.getGraphics();
		crayon.drawImage(
			images.get("background"),
			0, 0,
			im.getWidth(), im.getHeight(),
			null
		);

		for (int y = 0; y < game.getPlateau().getHauteur(); y++) {
			for (int x = 0; x < game.getPlateau().getLargeur(); x++) {
				if (game.getPlateau().estObstacle(x, y)) {
					crayon.drawImage(
						images.get("wall"),
						x * TAILLE_CASE,
						y * TAILLE_CASE,
						TAILLE_CASE,
						TAILLE_CASE,
						null
					);
				}
			}
		}

		crayon.drawImage(
			images.get("hero"),
			(int)(game.getHero().getX() * TAILLE_CASE),
			(int)(game.getHero().getY() * TAILLE_CASE),
			TAILLE_CASE,
			TAILLE_CASE,
			null
		);

		crayon.drawImage(
			images.get("sortie"),
			game.getPlateau().getSortie().x * TAILLE_CASE,
			game.getPlateau().getSortie().y * TAILLE_CASE,
			TAILLE_CASE,
			TAILLE_CASE,
			null
		);
	}

	@Override
	public int getWidth() {
		return game.getPlateau().getLargeur() * TAILLE_CASE;
	}

	@Override
	public int getHeight() {
		return game.getPlateau().getHauteur() * TAILLE_CASE;
	}

}
