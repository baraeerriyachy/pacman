
package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Block;
import fr.ul.acl.model.Trigger;
import fr.ul.acl.view.VisualEffect;

class BlockTest {

    private Trigger createTrigger() {
        return new Trigger(VisualEffect.getTexture("trap"));
    }

    @Test
    void emptyBlockIsCorrectlyInitialized() {
        Block block = Block.EMPTY;

        assertEquals(Block.Type.EMPTY, block.getType());
        assertNull(block.getTrigger());
        assertNull(block.getDestination());
        assertEquals(0, block.getDamage());
    }

    @Test
    void wallBlockContainsItsTrigger() {
        Trigger trigger = createTrigger();
        Block block = Block.wall(trigger);

        assertEquals(Block.Type.WALL, block.getType());
        assertSame(trigger, block.getTrigger());
        assertNull(block.getDestination());
        assertEquals(0, block.getDamage());
    }

    @Test
    void exitBlockIsCorrectlyInitialized() {
        Block block = Block.exit();

        assertEquals(Block.Type.EXIT, block.getType());
        assertNull(block.getTrigger());
        assertNull(block.getDestination());
        assertEquals(0, block.getDamage());
    }

    @Test
    void trapBlockContainsDamageAndTrigger() {
        Trigger trigger = createTrigger();
        Block block = Block.trap(20, trigger);

        assertEquals(Block.Type.TRAP, block.getType());
        assertEquals(20, block.getDamage());
        assertSame(trigger, block.getTrigger());
        assertNull(block.getDestination());
    }

    @Test
    void portalBlockContainsItsDestination() {
        Point destination = new Point(5, 8);
        Block block = Block.portal(destination);

        assertEquals(Block.Type.PORTAL, block.getType());
        assertEquals(destination, block.getDestination());
        assertNull(block.getTrigger());
        assertEquals(0, block.getDamage());
    }

    @Test
    void triggerBlockContainsItsTrigger() {
        Trigger trigger = createTrigger();
        Block block = Block.trigger(trigger);

        assertEquals(Block.Type.TRIGGER, block.getType());
        assertSame(trigger, block.getTrigger());
        assertNull(block.getDestination());
        assertEquals(0, block.getDamage());
    }
}
