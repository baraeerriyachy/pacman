package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.Monstre;

class MonstreTest {
    private static final float EPS = 0.0001f;

    @Test void zombieEstSolide() {
        Monstre m = Monstre.zombie(1, 2, 10, 0.1f);
        assertFalse(m.isIntangible());
        assertEquals(10, m.getDamage());
        assertEquals(1f, m.getPos().x, EPS);
    }

    @Test void fantomeEstIntangible() {
        Monstre m = Monstre.ghost(1, 2, 5, 0.3f);
        assertTrue(m.isIntangible());
        assertEquals(5, m.getDamage());
    }

    @Test void inversionDeDirectionEnFinDeTrajet() {
        Monstre m = new Monstre(1, 1, 10, 0.5f);
        m.setMaxMove(1);
        m.setDirection(1, 0);
        m.move();
        m.move();
        assertEquals(2f, m.getPos().x, EPS);
        m.move();
        assertEquals(1.5f, m.getPos().x, EPS);
    }

    @Test void directionAvanceALaBonneVitesse() {
        Monstre m = new Monstre(1, 2, 10, 0.1f);
        m.setMaxMove(10);
        m.setDirection(1, 0);
        m.move();
        assertEquals(1.1f, m.getPos().x, EPS);
        assertEquals(2f, m.getPos().y, EPS);
    }
}
