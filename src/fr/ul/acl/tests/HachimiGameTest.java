package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.GameLevel;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.Hero;
import fr.ul.acl.model.Position;

/** Jeu sans interface graphique: test sur une carte minimale controlee. */
class HachimiGameTest {
    private static final float EPS = 0.0001f;
    private final TestMapSupport maps = new TestMapSupport();
    private HachimiGame game;

    @BeforeEach void setUp() throws Exception {
        String map = maps.create("easy", """
                __ __ __ __ __
                __ HR __ WL __
                __ __ __ __ __
                __ EX __ __ __
                """);
        game = new HachimiGame(new GameLevel("ACL-Test", 1, map));
    }

    @AfterEach void tearDown() throws IOException { maps.clean(); }

    @Test void niveauNullEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> new HachimiGame(null));
    }

    @Test void partieInitialeEstValide() {
        assertNotNull(game.getPlateau());
        assertNotNull(game.getHero());
        assertEquals(5, game.getPlateau().getLargeur());
        assertEquals(4, game.getPlateau().getHauteur());
        assertFalse(game.isFinished());
        assertFalse(game.hasWon());
    }

    @Test void herosEstPlaceAuDepart() {
        assertEquals(1f, game.getHero().getPos().x, EPS);
        assertEquals(1f, game.getHero().getPos().y, EPS);
    }

    @Test void idleNeBougePas() {
        game.evolve(Cmd.IDLE);
        assertEquals(1f, game.getHero().getPos().x, EPS);
        assertEquals(1f, game.getHero().getPos().y, EPS);
    }

    @Test void commandeDroite() {
        game.evolve(Cmd.RIGHT);
        assertEquals(1.2f, game.getHero().getPos().x, EPS);
        assertEquals(1f, game.getHero().getPos().y, EPS);
    }

    @Test void commandeGauche() {
        game.evolve(Cmd.LEFT);
        assertEquals(0.8f, game.getHero().getPos().x, EPS);
    }

    @Test void commandeHaut() {
        game.evolve(Cmd.UP);
        assertEquals(0.8f, game.getHero().getPos().y, EPS);
    }

    @Test void commandeBas() {
        game.evolve(Cmd.DOWN);
        assertEquals(1.2f, game.getHero().getPos().y, EPS);
    }

    @Test void pasConsecutifsDroite() {
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        assertEquals(1.4f, game.getHero().getPos().x, EPS);
    }

    @Test void impossibleSortirParLaGauche() {
        game.getHero().teleport(0, 0);
        game.evolve(Cmd.LEFT);
        assertEquals(0f, game.getHero().getPos().x, EPS);
    }

    @Test void impossibleSortirParLeHaut() {
        game.getHero().teleport(0, 0);
        game.evolve(Cmd.UP);
        assertEquals(0f, game.getHero().getPos().y, EPS);
    }

    @Test void murBloqueLeDeplacement() {
        game.getHero().teleport(2, 1);
        game.evolve(Cmd.RIGHT);
        assertEquals(2.2f, game.getHero().getPos().x, EPS);
        game.evolve(Cmd.RIGHT);
        assertEquals(2.2f, game.getHero().getPos().x, EPS);
    }

    @Test void jeuEnPauseNeBougePas() {
        game.toggleStopped();
        game.evolve(Cmd.RIGHT);
        assertEquals(1f, game.getHero().getPos().x, EPS);
        game.toggleStopped();
        game.evolve(Cmd.RIGHT);
        assertEquals(1.2f, game.getHero().getPos().x, EPS);
    }

    @Test void redemarrageReinitialisePosition() throws Exception {
        Hero before = game.getHero();
        game.evolve(Cmd.RIGHT);
        game.recommencer();
        assertNotSame(before, game.getHero());
        assertEquals(1f, game.getHero().getPos().x, EPS);
        assertFalse(game.isFinished());
    }

    @Test void passageSurSortieSansInteractionNeGagnePas() {
        game.getHero().teleport(1, 2);
        game.evolve(Cmd.DOWN);
        game.evolve(Cmd.DOWN);
        assertFalse(game.hasWon());
    }

    @Test void interactionSurSortieFaitGagner() {
        game.getHero().teleport(1, 2);
        game.evolve(Cmd.DOWN);
        game.evolve(Cmd.DOWN);
        game.evolve(Cmd.INTERACT);
        assertTrue(game.hasWon());
        assertTrue(game.isFinished());
    }

    @Test void interactionSansActionPrealableNePlantePas() {
        // RIGHT-BICEP : erreur utilisateur possible des le demarrage.
        assertDoesNotThrow(() -> game.evolve(Cmd.INTERACT));
    }

    @Test void niveauSelectionneEstConserve() {
        assertEquals("ACL-Test", game.getLevel().getName());
    }
}
