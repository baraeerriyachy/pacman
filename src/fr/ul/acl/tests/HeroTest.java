package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Hero;

/**
 * Tests de Hero.
 * Rappel : Hero stocke des float et deplacer(nx, ny) DEFINIT la nouvelle
 * position (ce n'est pas un decalage a ajouter a la position actuelle).
 */
public class HeroTest {

    private static final float DELTA = 0.0001f;

    @Test
    public void positionInitiale() {
        Hero h = new Hero(2, 3);
        assertEquals(2f, h.getX(), DELTA);
        assertEquals(3f, h.getY(), DELTA);
    }

    @Test
    public void positionInitialeAvecDecimaux() {
        Hero h = new Hero(1.5f, 4.25f);
        assertEquals(1.5f, h.getX(), DELTA);
        assertEquals(4.25f, h.getY(), DELTA);
    }

    @Test
    public void deplacerDefinitLaNouvellePosition() {
        Hero h = new Hero(2, 3);
        h.deplacer(1, 0);
        assertEquals(1f, h.getX(), DELTA);
        assertEquals(0f, h.getY(), DELTA);
    }

    @Test
    public void deplacerAvecDesDecimaux() {
        Hero h = new Hero(0, 0);
        h.deplacer(5.2f, 7.4f);
        assertEquals(5.2f, h.getX(), DELTA);
        assertEquals(7.4f, h.getY(), DELTA);
    }

    @Test
    public void leDernierDeplacementGagne() {
        Hero h = new Hero(0, 0);
        h.deplacer(1, 1);
        h.deplacer(4, 2);
        assertEquals(4f, h.getX(), DELTA);
        assertEquals(2f, h.getY(), DELTA);
    }

    @Test
    public void vitesseParDefaut() {
        Hero h = new Hero(0, 0);
        assertEquals(0.2f, h.getVitesse(), DELTA);
    }

    @Test
    public void deplacerNeChangePasLaVitesse() {
        Hero h = new Hero(0, 0);
        h.deplacer(3, 3);
        assertEquals(0.2f, h.getVitesse(), DELTA);
    }
}