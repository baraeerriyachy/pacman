
package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Block;
import fr.ul.acl.model.BlockEffects;
import fr.ul.acl.model.Plateau;
import fr.ul.acl.model.Position;
import fr.ul.acl.model.Trigger;
import fr.ul.acl.view.VisualEffect;

class PlateauTest {

    private Trigger createTrigger() {
        return new Trigger(VisualEffect.getTexture("trap"));
    }

    // Constructor

    @Test
    void constructeurInitialiseUneGrilleVide() {
        Plateau plateau = new Plateau(10, 8);

        assertEquals(10, plateau.getLargeur());
        assertEquals(8, plateau.getHauteur());

        assertEquals(Block.Type.EMPTY, plateau.getBlockType(0, 0));
        assertEquals(Block.Type.EMPTY, plateau.getBlockType(5, 4));
        assertNull(plateau.getBlockTrigger(0, 0));
    }

    @Test
    void constructeurRefuseDesDimensionsInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Plateau(0, 5));

        assertThrows(IllegalArgumentException.class,
                () -> new Plateau(5, 0));

        assertThrows(IllegalArgumentException.class,
                () -> new Plateau(-1, 5));

        assertThrows(IllegalArgumentException.class,
                () -> new Plateau(5, -1));
    }

    // Blocks

    @Test
    void setBlockPlaceUnBlocDansLaGrille() {
        Plateau plateau = new Plateau(5, 5);
        Block block = Block.wall(null);

        plateau.setBlock(2, 3, block);

        assertEquals(Block.Type.WALL, plateau.getBlockType(2, 3));
    }

    @Test
    void getBlockTriggerRetourneLeTriggerDuBloc() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.setBlock(2, 2, Block.wall(trigger));

        assertSame(trigger, plateau.getBlockTrigger(2, 2));
    }

    // Boundaries

    @Test
    void isInsideMapVerifieLesLimitesDeLaCarte() {
        Plateau plateau = new Plateau(10, 8);

        assertTrue(plateau.isInsideMap(new Position(0, 0)));
        assertTrue(plateau.isInsideMap(new Position(9, 7)));

        assertFalse(plateau.isInsideMap(new Position(-1, 0)));
        assertFalse(plateau.isInsideMap(new Position(0, -1)));
        assertFalse(plateau.isInsideMap(new Position(10, 0)));
        assertFalse(plateau.isInsideMap(new Position(0, 8)));
    }

    // Collision

    @Test
    void uneCaseVideEstLibre() {
        Plateau plateau = new Plateau(5, 5);

        assertTrue(plateau.isFree(new Position(2, 2)));
    }

    @Test
    void unMurSansTriggerBloqueLePassage() {
        Plateau plateau = new Plateau(5, 5);

        plateau.setBlock(2, 2, Block.wall(null));

        assertFalse(plateau.isFree(new Position(2, 2)));
    }

    @Test
    void unMurAvecTriggerInactifBloqueLePassage() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.setBlock(2, 2, Block.wall(trigger));

        assertFalse(plateau.isFree(new Position(2, 2)));
    }

    @Test
    void unMurAvecTriggerActifDevientFranchissable() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.setBlock(2, 2, Block.wall(trigger));
        trigger.setActived();

        assertTrue(plateau.isFree(new Position(2, 2)));
    }

    // Light sources

    @Test
    void addLightSourceAjouteUneSourceDeLumiere() {
        Plateau plateau = new Plateau(5, 5);

        plateau.addLightSource(2, 3);
        plateau.addLightSource(4, 1);

        assertEquals(2, plateau.getLightSources().size());
        assertEquals(new Point(2, 3), plateau.getLightSources().get(0));
        assertEquals(new Point(4, 1), plateau.getLightSources().get(1));
    }

    // Interactions

    @Test
    void interactionAvecUnPiegeInfligeDesDegats() {
        Plateau plateau = new Plateau(5, 5);

        plateau.setBlock(2, 2, Block.trap(20, null));

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertEquals(20, effects.damage());
        assertFalse(effects.exit());
        assertFalse(plateau.isInteracable());
    }

    @Test
    void interactionAvecUnPiegeActiveSonTrigger() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.setBlock(2, 2, Block.trap(20, trigger));

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertEquals(20, effects.damage());
        assertTrue(trigger.isActivated());
    }

    @Test
    void unPiegeDejaActiveNInfligePlusDeDegats() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        trigger.setActived();
        plateau.setBlock(2, 2, Block.trap(20, trigger));

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertEquals(0, effects.damage());
    }

    @Test
    void interactionAvecUnPortailRetourneSaDestination() {
        Plateau plateau = new Plateau(5, 5);
        Point destination = new Point(4, 3);

        plateau.setBlock(2, 2, Block.portal(destination));

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertEquals(destination, effects.destination());
    }

    @Test
    void interactionAvecUneSortieSignaleLaFin() {
        Plateau plateau = new Plateau(5, 5);

        plateau.setBlock(2, 2, Block.exit());

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertTrue(effects.exit());
    }

    @Test
    void interactionAvecUnTriggerRetourneLeTrigger() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.setBlock(2, 2, Block.trigger(trigger));

        BlockEffects effects = plateau.interact(new Position(2, 2));

        assertSame(trigger, effects.trigger());
        assertTrue(plateau.isInteracable());
    }

    // Treasures

    @Test
    void addTreasureAjouteUnTresor() {
        Plateau plateau = new Plateau(5, 5);
        Trigger trigger = createTrigger();

        plateau.addTreasure(2, 2, Block.trigger(trigger));

        assertEquals(1, plateau.getNombreTotalTresors());
        assertEquals(0, plateau.getTresorsCollectes());
        assertEquals(Block.Type.TRIGGER, plateau.getBlockType(2, 2));
    }

    @Test
    void getTresorsCollectesCompteUniquementLesTresorsActifs() {
        Plateau plateau = new Plateau(5, 5);

        Trigger trigger1 = createTrigger();
        Trigger trigger2 = createTrigger();
        Trigger trigger3 = createTrigger();

        plateau.addTreasure(1, 1, Block.trigger(trigger1));
        plateau.addTreasure(2, 2, Block.trigger(trigger2));
        plateau.addTreasure(3, 3, Block.trigger(trigger3));

        trigger1.setActived();
        trigger3.setActived();

        assertEquals(3, plateau.getNombreTotalTresors());
        assertEquals(2, plateau.getTresorsCollectes());
    }
}
