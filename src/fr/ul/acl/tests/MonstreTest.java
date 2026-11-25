package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Monstre;

class MonstreTest {

    @Test
    void constructeurInitialiseLesAttributs() {
        Monstre monstre = new Monstre(2f, 3f, 20, 0.2f);

        assertEquals(20, monstre.getDamage());
        assertFalse(monstre.isIntangible());
        assertEquals(2f, monstre.getPos().x);
        assertEquals(3f, monstre.getPos().y);
    }

    @Test
    void zombieEstTangible() {
        Monstre zombie = Monstre.zombie(1f, 1f, 15, 0.2f);

        assertFalse(zombie.isIntangible());
        assertEquals(15, zombie.getDamage());
    }

    @Test
    void ghostEstIntangible() {
        Monstre ghost = Monstre.ghost(1f, 1f, 10, 0.3f);

        assertTrue(ghost.isIntangible());
        assertEquals(10, ghost.getDamage());
    }

    @Test
    void setMaxMoveInverseLaDirectionApresLaDistanceDefinie() {
        Monstre monstre = new Monstre(1f, 1f, 10, 0.2f);
        monstre.setMaxMove(0);

        monstre.setDirection(1, 0);
        monstre.move();

        // Direction should have been reversed.
        // A subsequent move should decrease the X position.
        float previousX = monstre.getPos().x;

        monstre.move();

        assertTrue(monstre.getPos().x < previousX);
    }

    @Test
    void moveMetAJourLaPosition() {
        Monstre monstre = new Monstre(1f, 1f, 10, 0.2f);
        monstre.setMaxMove(100);
        monstre.setDirection(1, 0);

        monstre.move();

        assertEquals(1.2f, monstre.getPos().x, 0.001f);
        assertEquals(1f, monstre.getPos().y, 0.001f);
    }
}