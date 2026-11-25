package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.Block;
import fr.ul.acl.model.GameLevel;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.Hero;
import fr.ul.acl.model.Position;
import fr.ul.acl.utility.MapReader;

/**
 * Tests du jeu : les coordonnees du depart, des murs et de la sortie
 * sont recherchees dans la carte, sans les imposer au heros.
 */
class HachimiGameTest {
    private static final float EPS = 0.0001f;
    private static final float PAS = 0.2f;
    private final TestMapSupport maps = new TestMapSupport();
    private HachimiGame game;
    private Point depart;

    @BeforeEach
    void setUp() throws Exception {
        String fichier = maps.create("easy", """
                __ __ __ __ __
                __ HR __ WL __
                __ __ __ __ __
                __ EX __ __ __
                """);
        MapReader lecteur = new MapReader(fichier);
        depart = TestPositionFinder.depart(lecteur);
        game = new HachimiGame(new GameLevel("ACL-Test", 1, fichier));
    }

    @AfterEach void tearDown() throws IOException { maps.clean(); }

    private void assertPosition(float x, float y) {
        Position p = game.getHero().getPos();
        assertEquals(x, p.x, EPS);
        assertEquals(y, p.y, EPS);
    }

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
        assertPosition(depart.x, depart.y);
    }

    @Test void idleNeBougePas() {
        Position avant = game.getHero().getPos();
        float x = avant.x, y = avant.y;
        game.evolve(Cmd.IDLE);
        assertPosition(x, y);
    }

    @Test void commandeDroite() {
        game.evolve(Cmd.RIGHT);
        assertPosition(depart.x + PAS, depart.y);
    }

    @Test void commandeGauche() {
        game.evolve(Cmd.LEFT);
        assertPosition(depart.x - PAS, depart.y);
    }

    @Test void commandeHaut() {
        game.evolve(Cmd.UP);
        assertPosition(depart.x, depart.y - PAS);
    }

    @Test void commandeBas() {
        game.evolve(Cmd.DOWN);
        assertPosition(depart.x, depart.y + PAS);
    }

    @Test void pasConsecutifsDroite() {
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        assertPosition(depart.x + 2 * PAS, depart.y);
    }

    @Test void impossibleSortirParLaGauche() {
        Point bord = TestPositionFinder.trouverCaseLibreSurBord(
                game.getPlateau(), TestPositionFinder.Bord.GAUCHE);
        TestPositionFinder.rejoindre(game, bord);
        float x = game.getHero().getPos().x;
        float y = game.getHero().getPos().y;
        game.evolve(Cmd.LEFT);
        assertPosition(x, y);
    }

    @Test void impossibleSortirParLeHaut() {
        Point bord = TestPositionFinder.trouverCaseLibreSurBord(
                game.getPlateau(), TestPositionFinder.Bord.HAUT);
        TestPositionFinder.rejoindre(game, bord);
        float x = game.getHero().getPos().x;
        float y = game.getHero().getPos().y;
        game.evolve(Cmd.UP);
        assertPosition(x, y);
    }

    @Test void murBloqueLeDeplacement() {
        TestPositionFinder.ApprocheMur approche =
                TestPositionFinder.trouverApprocheMur(game.getPlateau());
        TestPositionFinder.rejoindre(game, approche.caseAvantMur());
        boolean bloque = false;
        for (int i = 0; i < 10; i++) {
            Position avant = game.getHero().getPos();
            float x = avant.x, y = avant.y;
            game.evolve(approche.directionVersMur());
            Position apres = game.getHero().getPos();
            if (Math.abs(apres.x - x) < EPS && Math.abs(apres.y - y) < EPS) {
                bloque = true;
                break;
            }
        }
        assertTrue(bloque, "Le mur devrait interrompre le deplacement du heros");
    }

    @Test void jeuEnPauseNeBougePas() {
        float x = game.getHero().getPos().x;
        float y = game.getHero().getPos().y;
        game.toggleStopped();
        game.evolve(Cmd.RIGHT);
        assertPosition(x, y);
        game.toggleStopped();
        game.evolve(Cmd.RIGHT);
        assertPosition(x + PAS, y);
    }

    @Test void redemarrageReinitialisePosition() throws Exception {
        Hero before = game.getHero();
        game.evolve(Cmd.RIGHT);
        game.recommencer();
        assertNotSame(before, game.getHero());
        assertPosition(depart.x, depart.y);
        assertFalse(game.isFinished());
    }

    @Test void passageSurSortieSansInteractionNeGagnePas() {
        Point sortie = TestPositionFinder.trouverBloc(game.getPlateau(), Block.Type.EXIT);
        TestPositionFinder.rejoindre(game, sortie);
        assertFalse(game.hasWon());
    }

    @Test void interactionSurSortieFaitGagner() {
        Point sortie = TestPositionFinder.trouverBloc(game.getPlateau(), Block.Type.EXIT);
        TestPositionFinder.rejoindre(game, sortie);
        game.evolve(Cmd.INTERACT);
        assertTrue(game.hasWon());
        assertTrue(game.isFinished());
    }

    @Test void interactionSansActionPrealableNePlantePas() {
        // Test d'anomalie conserve : le jeu actuel lance une NullPointerException.
        assertDoesNotThrow(() -> game.evolve(Cmd.INTERACT));
    }

    @Test void niveauSelectionneEstConserve() {
        assertEquals("ACL-Test", game.getLevel().getName());
    }
}
