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
	private Map<String, Image> heroAnimations = new HashMap<>();

	private void loadImage(String name, String filename) {
		URL url = getClass().getResource("/" + filename);

		if (url == null) {
			throw new IllegalStateException(filename + " not found!");
		}

		images.put(name, new ImageIcon(url).getImage());
	}

	private void loadHeroAnimation(String name, String filename) {
		URL url = getClass().getResource("/hero/" + filename);

		if (url == null) {
			throw new IllegalStateException(filename + " not found!");
		}

		heroAnimations.put(name, new ImageIcon(url).getImage());
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
		loadImage("wall", "rock.png");
		loadImage("sortie", "sortie.png");
		loadImage("trap", "trap.gif");
		loadImage("portal", "portal.gif");
		loadImage("zombie", "zombie.gif");
		loadImage("ghost", "ghost.gif");

		String[] directions = {"back", "front", "left", "right"};
		String[] states = {"idle", "walk", "hurt"};

		for (String direction : directions) {
			for (String state : states) {
				String animation = state + "_" + direction;
				loadHeroAnimation(animation, animation + ".gif");
			}
		}
	}

	/**
	 * methode  redefinie de Afficheur retourne une image du jeu
	 */
	@Override
	public void draw(BufferedImage im) {
		Graphics2D crayon = (Graphics2D) im.getGraphics();
		drawBackground(crayon, im.getWidth(), im.getHeight());

		drawPlateau(crayon);
		
		for (Monstre m : game.getMonstres()) {
			drawEntity(crayon, images.get(m.isIntangible() ? "ghost" : "zombie"), m);
		}

		drawHero(crayon);

		drawHealthBar(crayon, game.getHero());
	}

	private void drawHero(Graphics2D crayon){
		Hero hero = game.getHero();
		String animation = hero.getAnimationState() + "_" + hero.getAnimationDirection();
		drawEntity(crayon, heroAnimations.get(animation), hero);
	}

	private void drawPlateau(Graphics2D crayon){
		for (int y = 0; y < game.getPlateau().getHauteur(); y++) {
			for (int x = 0; x < game.getPlateau().getLargeur(); x++) {
				// String image = null;
				// switch (blockType) {
				// 	case WALL: image = "wall"; break;
				// 	case TRAP: image = "trap"; break;
				// 	case PORTAL: image = "portal";break;
				// 	case EMPTY:break;
				// }
				String image = switch (game.getPlateau().getBlockType(x, y)) {
					case WALL -> "wall";
					case TRAP -> "trap";
					case PORTAL -> "portal";
					case EMPTY -> null;
				};

				if (image != null) {
					drawBlock(crayon,images.get(image),x,y);
				}
			}
		}
		drawBlock(crayon, images.get("sortie"),game.getPlateau().getSortie().x, game.getPlateau().getSortie().y);
	}

	private void drawBackground(Graphics2D crayon, int w, int h){
		crayon.drawImage(
			images.get("background"),
			0, 0, w, h,
			null
		);
	}


	private void drawEntity(Graphics2D crayon, Image image, Entity e){
		drawUnit(
			crayon, image, 
			(int)(e.getPos().x * TAILLE_CASE),
			(int)(e.getPos().y * TAILLE_CASE)
		);
	}
	private void drawBlock(Graphics2D crayon, Image image, int x, int y){
		drawUnit(
			crayon, image, 
			x * TAILLE_CASE,
			y * TAILLE_CASE
		);
	}

	private void drawUnit(Graphics2D crayon, Image image, int x, int y){
		crayon.drawImage(
			image,x,y,
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
