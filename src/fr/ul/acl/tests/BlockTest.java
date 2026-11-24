package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.Block;
import fr.ul.acl.model.Trigger;

class BlockTest {
    @Test void caseVide() {
        assertEquals(Block.Type.EMPTY, Block.EMPTY.getType());
        assertEquals(0, Block.EMPTY.getDamage());
        assertNull(Block.EMPTY.getDestination());
    }

    @Test void mur() {
        Block block = Block.wall(null);
        assertEquals(Block.Type.WALL, block.getType());
        assertNull(block.getTrigger());
    }

    @Test void piege() {
        Block block = Block.trap(30, null);
        assertEquals(Block.Type.TRAP, block.getType());
        assertEquals(30, block.getDamage());
    }

    @Test void portail() {
        Point p = new Point(8, 2);
        Block block = Block.portal(p);
        assertEquals(Block.Type.PORTAL, block.getType());
        assertEquals(p, block.getDestination());
    }

    @Test void sortie() {
        assertEquals(Block.Type.EXIT, Block.exit().getType());
    }

    @Test void declencheur() {
        Trigger trigger = new Trigger(null);
        Block block = Block.trigger(trigger);
        assertEquals(Block.Type.TRIGGER, block.getType());
        assertSame(trigger, block.getTrigger());
    }
}
