package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.Position;

class PositionTest {
    private static final float EPS = 0.0001f;

    @Test void constructeurEtSet() {
        Position p = new Position(1.5f, 2f);
        assertEquals(1.5f, p.x, EPS);
        p.set(3f, 4.5f);
        assertEquals(3f, p.x, EPS);
        assertEquals(4.5f, p.y, EPS);
    }

    @Test void chevauchementMemePosition() {
        assertTrue(new Position(2, 2).isOverlapping(new Position(2, 2)));
    }

    @Test void chevauchementPartiel() {
        assertTrue(new Position(2, 2).isOverlapping(new Position(2.5f, 2)));
    }

    @Test void objetsSeparesNeSeChevauchentPas() {
        assertFalse(new Position(2, 2).isOverlapping(new Position(4, 2)));
    }

    @Test void limitesDesCases() {
        Position p = new Position(2f, 3f);
        assertEquals(2, p.xPrev());
        assertEquals(2, p.xNext());
        assertEquals(3, p.yPrev());
        assertEquals(3, p.yNext());
    }
}
