package fr.ul.acl.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import java.net.URL;

import fr.ul.acl.engine.GamePainter;

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
		loadImage("trap", "trap.png");
		loadImage("portal", "portal.gif");
		loadImage("zombie", "zombie.gif");
		loadImage("ghost", "ghost.gif");
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
				String image = switch (game.getPlateau().getBlockType(x, y)) {
					case WALL -> "wall";
					case TRAP -> "trap";
					case PORTAL -> "portal";
					case EMPTY -> null;
				};

				if (image != null) {
					crayon.drawImage(
						images.get(image),
						x * TAILLE_CASE,
						y * TAILLE_CASE,
						TAILLE_CASE,
						TAILLE_CASE,
						null
					);
				}
			}
		}

		for (Monstre m : game.getMonstres()) {
			crayon.drawImage(
				images.get(m.isIntangible() ? "ghost" : "zombie"),
				(int)(m.getPos().x * TAILLE_CASE),
				(int)(m.getPos().y * TAILLE_CASE),
				TAILLE_CASE,
				TAILLE_CASE,
				null
			);
		}

		crayon.drawImage(
			images.get("hero"),
			(int)(game.getHero().getPos().x * TAILLE_CASE),
			(int)(game.getHero().getPos().y * TAILLE_CASE),
			TAILLE_CASE,
			TAILLE_CASE,
			null
		);

		drawHealthBar(crayon, game.getHero());

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

	private void drawHealthBar(Graphics2D g2, Hero hero) {
		int x = 20;
		int y = 20;
		int width = 120;
		int height = 20;

		int maxHp = hero.getMaxHp();
		int currentHp = hero.getHealthPoints();

		// Background
		g2.setColor(Color.DARK_GRAY);
		g2.fillRoundRect(x, y, width, height, 10, 10);

		// Current health
		int healthWidth = (int) ((double) currentHp / maxHp * width);

		g2.setColor(Color.GREEN);
		g2.fillRoundRect(x, y, healthWidth, height, 10, 10);

		// Border
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(x, y, width, height, 10, 10);

		// HP text
		String text = currentHp + " / " + maxHp;
		g2.setFont(new Font("Arial", Font.BOLD, 12));

		FontMetrics fm = g2.getFontMetrics();
		int textX = x + (width - fm.stringWidth(text)) / 2;
		int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();

		g2.drawString(text, textX, textY);
	}
}
