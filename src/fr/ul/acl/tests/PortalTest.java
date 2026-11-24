package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.*;
import fr.ul.acl.utility.MapReader;

/** Sprint 3 : tests de la porte de teleportation. */
class PortalTest {
    private final TestMapSupport maps = new TestMapSupport();
    private static final float EPS = 0.0001f;

    private String map() throws Exception {
        return maps.create("easy", """
                __ __ __ __ __
                __ HR P1 __ __
                __ __ __ __ __
                __ __ D1 EX __
                """);
    }

    @AfterEach void clean() throws IOException { maps.clean(); }

    @Test void lecteurAssociePortailADestination() throws Exception {
        MapReader reader = new MapReader(map());
        Plateau p = reader.getPlateau();
        assertEquals(Block.Type.PORTAL, p.getBlockType(2, 1));
        assertEquals(new Point(2, 3), p.interact(new Position(2, 1)).destination());
    }

    @Test void interactionAvecPortailDeclencheEffet() throws Exception {
        HachimiGame game = new HachimiGame(new GameLevel("Portail", 1, map()));
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.INTERACT);
        assertEquals("lightning", game.getVisualEffect());
        assertEquals(1.4f, game.getHero().getPos().x, EPS);
    }

    @Test void finAnimationTeleporteVersDestination() throws Exception {
        HachimiGame game = new HachimiGame(new GameLevel("Portail", 1, map()));
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.INTERACT);
        game.unsetVisual();
        assertEquals(2f, game.getHero().getPos().x, EPS);
        assertEquals(3f, game.getHero().getPos().y, EPS);
        assertEquals("", game.getVisualEffect());
    }
}
