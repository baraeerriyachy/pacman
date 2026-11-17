
package fr.ul.acl.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import java.net.URL;

import fr.ul.acl.engine.GamePainter;
import fr.ul.acl.model.*;

public class HachimiPainter implements GamePainter {

    private static final int TAILLE_CASE = 60;

    // Fixed viewport dimensions
    private static final int MAX_WIDTH = 960;
	private static final int MAX_HEIGHT = 720;

	private final int screenWidth;
	private final int screenHeight;
	private final boolean scroll;

    // Lighting
    private static final int LIGHT_RADIUS = 2 * TAILLE_CASE;

    private HachimiGame game;

    private int cameraX = 0;
    private int cameraY = 0;

    private Map<String, Image> images = new HashMap<>();
    private Map<String, VisualEffect> visuals = new HashMap<>();

    private void loadImage(String name, String filename) {
        URL url = getClass().getResource("/" + filename);

        if (url == null) {
            throw new IllegalStateException(filename + " not found!");
        }

        images.put(name, new ImageIcon(url).getImage());
    }

    /**
     * Constructeur du painter.
     *
     * @param game le jeu à afficher
     */
    public HachimiPainter(HachimiGame game) {
        this.game = game;
		int mapWidth = game.getPlateau().getLargeur() * TAILLE_CASE;
		int mapHeight = game.getPlateau().getHauteur() * TAILLE_CASE;

		scroll = mapWidth > MAX_WIDTH || mapHeight > MAX_HEIGHT;

		screenWidth = Math.min(mapWidth, MAX_WIDTH);
		screenHeight = Math.min(mapHeight, MAX_HEIGHT);

        loadImage("background", "background.jpg");
        loadImage("wall", "texture/rock.png");
        loadImage("exit", "exit.gif");
        loadImage("trap", "texture/trap.gif");
        loadImage("portal", "texture/portal.gif");
        loadImage("torch", "texture/torch.gif");
        loadImage("zombie", "ennemy/zombie.gif");
        loadImage("ghost", "ennemy/ghost.gif");

        String[] effectNames = {"lightning", "explosion"};

        for (String name : effectNames) {
            visuals.put(name, new VisualEffect("effect/" + name + ".gif"));
        }

        String[] directions = {"back", "front", "left", "right"};
        String[] states = {"idle", "walk", "hurt"};

        for (String direction : directions) {
            for (String state : states) {
                String animation = state + "_" + direction;
                loadImage(animation, "hero/" + animation + ".gif");
            }
        }
    }

    /**
     * Dessine le jeu dans une fenêtre de dimensions fixes.
     */
    @Override
    public void draw(BufferedImage im) {

		if(scroll)
        	updateCamera();

        Graphics2D screen = im.createGraphics();

        // Background
        drawBackground(screen, im.getWidth(), im.getHeight());

        // World rendering (map coordinates)
        Graphics2D world = (Graphics2D) screen.create();
        world.translate(-cameraX, -cameraY);

        drawPlateau(world);

        for (Monstre m : game.getMonstres()) {
            drawEntity(
                world,
                images.get(m.isIntangible() ? "ghost" : "zombie"),
                m
            );
        }

        world.dispose();

        // Darkness overlay
		if(game.getLevel().isDark())
        	drawDarkness(screen, im.getWidth(), im.getHeight());

        // Hero remains visible above the darkness
        Graphics2D heroLayer = (Graphics2D) screen.create();
        heroLayer.translate(-cameraX, -cameraY);

        drawHero(heroLayer);
        drawEffect(heroLayer);

        heroLayer.dispose();

        // HUD (screen coordinates)
        drawHealthBar(screen, game.getHero());
        drawMessage(screen);

        screen.dispose();
    }

    /**
     * Updates the camera to follow the hero.
     * The camera stops at map boundaries.
     */
    private void updateCamera() {

        Hero hero = game.getHero();

        int mapWidth =
            game.getPlateau().getLargeur() * TAILLE_CASE;

        int mapHeight =
            game.getPlateau().getHauteur() * TAILLE_CASE;

        int targetX =
            (int) (hero.getPos().x * TAILLE_CASE)
            - screenWidth / 2
            + TAILLE_CASE / 2;

        int targetY =
            (int) (hero.getPos().y * TAILLE_CASE)
            - screenHeight / 2
            + TAILLE_CASE / 2;

        cameraX = Math.max(
            0,
            Math.min(targetX, mapWidth - screenWidth)
        );

        cameraY = Math.max(
            0,
            Math.min(targetY, mapHeight - screenHeight)
        );
    }

    /**
     * Applies darkness outside the hero's visibility radius.
     */
    private void drawDarkness(
        Graphics2D g2,
        int width,
        int height
    ) {
        BufferedImage darkness = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D dark = darkness.createGraphics();

        // Full darkness
        dark.setColor(new Color(0, 0, 0, 245));
        dark.fillRect(0, 0, width, height);

        // Erase darkness progressively around the hero
        Position p = game.getHero().getPos();
        removeDarkness(dark, p.x, p.y, LIGHT_RADIUS);

        // Erase darkness around torches
        for (Point source : game.getPlateau().getLightSources()) {
            drawBlock(g2, images.get("torch"), source.x, source.y);
            removeDarkness(dark, source.x, source.y, LIGHT_RADIUS);
        }

        dark.dispose();

        g2.drawImage(darkness, 0, 0, null);
    }

    private void removeDarkness(Graphics2D g2, float xx, float yy, int radius) {
        int x = (int) (xx * TAILLE_CASE)
            + TAILLE_CASE / 2
            - cameraX;

        int y = (int) (yy * TAILLE_CASE)
            + TAILLE_CASE / 2
            - cameraY;

        float[] fractions = {0f, 0.6f, 1f};

        Color[] colors = {
            new Color(0, 0, 0, 245),
            new Color(0, 0, 0, 100),
            new Color(0, 0, 0, 0)
        };

        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point(x, y),
            radius,
            fractions,
            colors
        );

        g2.setComposite(AlphaComposite.DstOut);
        g2.setPaint(gradient);
        g2.fillOval(
            x - radius, y - radius,
            radius * 2, radius * 2
        );

        g2.setComposite(AlphaComposite.SrcOver);
    }

    private void drawEffect(Graphics2D crayon) {

        String effectName = game.getVisualEffect();

        if (effectName.isEmpty())
            return;

        VisualEffect visual = visuals.get(effectName);

        BufferedImage beam = visual.getCurrentFrame();

        int width = 3 * TAILLE_CASE;

        int height = (int) (
            beam.getHeight()
            * (width / (double) beam.getWidth())
        );

        crayon.drawImage(
            beam,
            (game.getHero().getPos().xPrev() - 1) * TAILLE_CASE,
            (game.getHero().getPos().yPrev() + 1) * TAILLE_CASE - height,
            width,
            height,
            null
        );

        if (visual.isFinished()) {
            visual.reset();
            game.unsetVisual();
        }

        visual.nextFrame();
    }

    private void drawMessage(Graphics2D crayon) {

        crayon.setFont(new Font("Arial", Font.BOLD, 18));

        if (!game.getMessage().isEmpty()) {

            crayon.setColor(Color.WHITE);

            crayon.drawString(
                game.getMessage(),
                20,
                screenHeight - 20
            );
        }
    }

    private void drawHero(Graphics2D crayon) {

        Hero hero = game.getHero();

        String animation =
            hero.getAnimationState()
            + "_"
            + hero.getAnimationDirection();

        drawEntity(
            crayon,
            images.get(animation),
            hero
        );
    }

    private void drawPlateau(Graphics2D crayon) {

        Plateau p = game.getPlateau();

        for (int y = 0; y < p.getHauteur(); y++) {

            for (int x = 0; x < p.getLargeur(); x++) {

                String image = switch (p.getBlockType(x, y)) {
                    case WALL -> "wall";
                    case TRAP -> "trap";
                    case PORTAL -> "portal";
                    case EXIT -> "exit";
                    case TRIGGER -> "";
                    case EMPTY -> null;
                };

                Trigger t = p.getBlockTrigger(x, y);

                if (image == null)
                    continue;

                else if (t != null)
                    drawBlock(crayon, t.getImage(), x, y);

                else
                    drawBlock(crayon, images.get(image), x, y);
            }
        }
    }

    private void drawBackground(
        Graphics2D crayon,
        int w,
        int h
    ) {

        crayon.drawImage(
            images.get("background"),
            0, 0, w, h,
            null
        );
		// crayon.setColor(new Color(0, 0, 0));
    	// crayon.fillRect(0, 0, w, h);
    }

    private void drawEntity(
        Graphics2D crayon,
        Image image,
        Entity e
    ) {

        drawUnit(
            crayon,
            image,
            (int) (e.getPos().x * TAILLE_CASE),
            (int) (e.getPos().y * TAILLE_CASE)
        );
    }

    private void drawBlock(
        Graphics2D crayon,
        Image image,
        int x,
        int y
    ) {

        drawUnit(
            crayon,
            image,
            x * TAILLE_CASE,
            y * TAILLE_CASE
        );
    }

    private void drawUnit(
        Graphics2D crayon,
        Image image,
        int x,
        int y
    ) {

        crayon.drawImage(
            image,
            x,
            y,
            TAILLE_CASE,
            TAILLE_CASE,
            null
        );
    }

    @Override
    public int getWidth() {
        return screenWidth;
    }

    @Override
    public int getHeight() {
        return screenHeight;
    }

    /**
     * Draws the hero health bar in screen coordinates.
     */
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
        int healthWidth =
            (int) ((double) currentHp / maxHp * width);

        g2.setColor(Color.GREEN);
        g2.fillRoundRect(x, y, healthWidth, height, 10, 10);

        // Border
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, width, height, 10, 10);

        // HP text
        String text = currentHp + " / " + maxHp;

        g2.setFont(new Font("Arial", Font.BOLD, 12));

        FontMetrics fm = g2.getFontMetrics();

        int textX =
            x + (width - fm.stringWidth(text)) / 2;

        int textY =
            y + (height - fm.getHeight()) / 2
            + fm.getAscent();

        g2.drawString(text, textX, textY);
    }
}
