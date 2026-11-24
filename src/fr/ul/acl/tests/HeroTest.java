package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.Hero;
import fr.ul.acl.model.Position;

/** Tests unitaires de Hero (API actuelle: Entity + Position). */
class HeroTest {
    private static final float EPS = 0.0001f;

    @Test void positionInitialeEntiere() {
        Hero hero = new Hero(2, 3);
        assertEquals(2f, hero.getPos().x, EPS);
        assertEquals(3f, hero.getPos().y, EPS);
    }

    @Test void positionInitialeDecimale() {
        Hero hero = new Hero(1.5f, 4.25f);
        assertEquals(1.5f, hero.getPos().x, EPS);
        assertEquals(4.25f, hero.getPos().y, EPS);
    }

    @Test void deplacementAvecDirectionDroite() {
        Hero hero = new Hero(2, 3);
        hero.setDirection(1, 0);
        Position prochaine = hero.getNewPos();
        assertEquals(2.2f, prochaine.x, EPS);
        assertEquals(3f, prochaine.y, EPS);
        hero.move();
        assertEquals(2.2f, hero.getPos().x, EPS);
    }

    @Test void deplacementVersLeHaut() {
        Hero hero = new Hero(2, 3);
        hero.setDirection(0, -1);
        hero.move();
        assertEquals(2f, hero.getPos().x, EPS);
        assertEquals(2.8f, hero.getPos().y, EPS);
    }

    @Test void directionNulleNeBougePas() {
        Hero hero = new Hero(2, 3);
        hero.setDirection(0, 0);
        hero.move();
        assertEquals(2f, hero.getPos().x, EPS);
        assertEquals(3f, hero.getPos().y, EPS);
        assertEquals("idle", hero.getAnimationState());
    }

    @Test void teleportationDefinitLaPosition() {
        Hero hero = new Hero(2, 3);
        hero.teleport(7, 5);
        assertEquals(7f, hero.getPos().x, EPS);
        assertEquals(5f, hero.getPos().y, EPS);
    }

    @Test void changementDirectionChangeAnimation() {
        Hero hero = new Hero(0, 0);
        hero.setDirection(1, 0);
        assertEquals("walk", hero.getAnimationState());
        assertEquals("right", hero.getAnimationDirection());
        hero.setDirection(0, 0);
        assertEquals("idle", hero.getAnimationState());
    }

    @Test void pointsDeVieAuDemarrage() {
        Hero hero = new Hero(0, 0);
        assertEquals(100, hero.getMaxHp());
        assertEquals(100, hero.getHealthPoints());
        assertFalse(hero.isDead());
    }

    @Test void subirDesDegatsReduitLesPointsDeVie() {
        Hero hero = new Hero(0, 0);
        assertTrue(hero.takeDamage(25));
        assertEquals(75, hero.getHealthPoints());
        assertEquals("hurt", hero.getAnimationState());
    }

    @Test void degatsNulsOuNegatifsSontRejetes() {
        Hero hero = new Hero(0, 0);
        assertFalse(hero.takeDamage(0));
        assertFalse(hero.takeDamage(-5));
        assertEquals(100, hero.getHealthPoints());
    }

    @Test void invulnerabiliteBloqueDeuxDegatsImmediats() {
        Hero hero = new Hero(0, 0);
        assertTrue(hero.takeDamage(10));
        assertFalse(hero.takeDamage(20));
        assertEquals(90, hero.getHealthPoints());
    }

    @Test void degatsMortelsNeDonnePasPointsDeVieNegatifs() {
        Hero hero = new Hero(0, 0);
        assertTrue(hero.takeDamage(500));
        assertEquals(0, hero.getHealthPoints());
        assertTrue(hero.isDead());
        assertFalse(hero.takeDamage(1));
    }
}
