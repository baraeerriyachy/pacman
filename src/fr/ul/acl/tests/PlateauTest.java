package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.*;

/** Conditions normales, limites et interactions du plateau actuel. */
class PlateauTest {
    private Plateau plateau() { return new Plateau(10, 8); }

    @Test void dimensions() {
        Plateau p = plateau();
        assertEquals(10, p.getLargeur());
        assertEquals(8, p.getHauteur());
    }

    @Test void dimensionsInvalides() {
        assertThrows(IllegalArgumentException.class, () -> new Plateau(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(5, -1));
    }

    @Test void toutesLesCasesSontInitialementVides() {
        Plateau p = plateau();
        for (int x = 0; x < p.getLargeur(); x++)
            for (int y = 0; y < p.getHauteur(); y++)
                assertEquals(Block.Type.EMPTY, p.getBlockType(x, y));
    }

    @Test void coinsSontDansLesLimites() {
        Plateau p = plateau();
        assertTrue(p.isInsideMap(new Position(0, 0)));
        assertTrue(p.isInsideMap(new Position(9, 0)));
        assertTrue(p.isInsideMap(new Position(0, 7)));
        assertTrue(p.isInsideMap(new Position(9, 7)));
    }

    @Test void limitesNegativesRejetees() {
        Plateau p = plateau();
        assertFalse(p.isInsideMap(new Position(-0.01f, 0)));
        assertFalse(p.isInsideMap(new Position(0, -0.01f)));
    }

    @Test void limitesDroiteEtBas() {
        Plateau p = plateau();
        assertFalse(p.isInsideMap(new Position(9.01f, 0)));
        assertFalse(p.isInsideMap(new Position(0, 7.01f)));
        assertFalse(p.isInsideMap(new Position(10, 0)));
        assertFalse(p.isInsideMap(new Position(0, 8)));
    }

    @Test void largeurEtHauteurNonInversees() {
        Plateau p = plateau();
        assertTrue(p.isInsideMap(new Position(9, 0)));
        assertFalse(p.isInsideMap(new Position(0, 9)));
    }

    @Test void murBloqueEtCaseVideNeBloquePas() {
        Plateau p = plateau();
        p.setBlock(3, 3, Block.wall(null));
        assertEquals(Block.Type.WALL, p.getBlockType(3, 3));
        assertFalse(p.isFree(new Position(3, 3)));
        assertTrue(p.isFree(new Position(4, 3)));
        assertTrue(p.isFree(new Position(2, 3)));
    }

    @Test void obstacleNonTranspose() {
        Plateau p = plateau();
        p.setBlock(3, 2, Block.wall(null));
        assertEquals(Block.Type.WALL, p.getBlockType(3, 2));
        assertEquals(Block.Type.EMPTY, p.getBlockType(2, 3));
    }

    @Test void chevauchementMurSelonMargeDuJeu() {
        Plateau p = plateau();
        p.setBlock(3, 3, Block.wall(null));
        assertTrue(p.isFree(new Position(2f, 3f)));
        assertFalse(p.isFree(new Position(2.25f, 3f)));
    }

    @Test void zoneLibreEntreDeuxMurs() {
        Plateau p = plateau();
        p.setBlock(3, 3, Block.wall(null));
        p.setBlock(5, 3, Block.wall(null));
        assertTrue(p.isFree(new Position(4, 3)));
        assertFalse(p.isFree(new Position(4.25f, 3)));
    }

    @Test void interactionAvecSortie() {
        Plateau p = plateau();
        p.setBlock(2, 3, Block.exit());
        assertTrue(p.interact(new Position(2, 3)).exit());
        assertFalse(p.interact(new Position(1, 3)).exit());
    }

    @Test void interactionAvecPiege() {
        Plateau p = plateau();
        p.setBlock(3, 2, Block.trap(12, null));
        BlockEffects effects = p.interact(new Position(3, 2));
        assertEquals(12, effects.damage());
        assertFalse(effects.exit());
    }

    @Test void interactionAvecPortail() {
        Plateau p = plateau();
        Point destination = new Point(7, 6);
        p.setBlock(2, 3, Block.portal(destination));
        assertEquals(destination, p.interact(new Position(2, 3)).destination());
    }

    @Test void tresorNonActiveCompteDansLeTotal() {
        Plateau p = plateau();
        p.addTreasure(2, 3, Block.trigger(new Trigger(null)));
        assertEquals(1, p.getNombreTotalTresors());
        assertEquals(0, p.getTresorsCollectes());
        assertEquals(Block.Type.TRIGGER, p.getBlockType(2, 3));
    }

    @Test void sourceDeLumiereEnregistree() {
        Plateau p = plateau();
        p.addLightSource(1, 2);
        assertTrue(p.getLightSources().contains(new Point(1, 2)));
    }
}
