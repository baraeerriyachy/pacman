package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Hero;

public class HeroTest {

    @Test
    public void positionInitiale() {
        Hero h = new Hero(2, 3);
        assertEquals(2, h.getX());
        assertEquals(3, h.getY());
    }

    @Test
    public void deplacementADroite() {
        Hero h = new Hero(2, 3);
        h.deplacer(1, 0);
        assertEquals(3, h.getX());
        assertEquals(3, h.getY());
    }

    @Test
    public void deplacementEnBas() {
        Hero h = new Hero(2, 3);
        h.deplacer(0, 1);
        assertEquals(2, h.getX());
        assertEquals(4, h.getY());
    }
}