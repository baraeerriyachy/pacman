package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Hero;
import fr.ul.acl.model.Plateau;
import fr.ul.acl.model.Position;
import fr.ul.acl.utility.MapReader;

/** Tests unitaires du heros : position initiale recherchee dans une vraie carte. */
class HeroTest {
    private static final float EPS = 0.0001f;
    private static final float PAS = 0.2f;
    private Point depart;
    private Plateau plateau;

    @BeforeEach
    void chargerCarte() throws IOException {
        MapReader lecteur = new MapReader("easy/level1.txt");
        depart = TestPositionFinder.depart(lecteur);
        plateau = lecteur.getPlateau();
    }

    private Hero nouveauHero() {
        return new Hero(depart.x, depart.y);
    }

    @Test void positionInitialeEntiere() {
        Hero hero = nouveauHero();
        assertEquals(depart.x, hero.getPos().x, EPS);
        assertEquals(depart.y, hero.getPos().y, EPS);
    }

    @Test void positionInitialeDecimale() {
        Point autre = TestPositionFinder.trouverCaseLibre(plateau, depart);
        float x = (depart.x + autre.x) / 2f;
        float y = (depart.y + autre.y) / 2f;
        Hero hero = new Hero(x, y);
        assertEquals(x, hero.getPos().x, EPS);
        assertEquals(y, hero.getPos().y, EPS);
    }

    @Test void deplacementAvecDirectionDroite() {
        Hero hero = nouveauHero();
        hero.setDirection(1, 0);
        Position prochaine = hero.getNewPos();
        assertEquals(depart.x + PAS, prochaine.x, EPS);
        assertEquals(depart.y, prochaine.y, EPS);
        hero.move();
        assertEquals(depart.x + PAS, hero.getPos().x, EPS);
    }

    @Test void deplacementVersLeHaut() {
        Hero hero = nouveauHero();
        hero.setDirection(0, -1);
        hero.move();
        assertEquals(depart.x, hero.getPos().x, EPS);
        assertEquals(depart.y - PAS, hero.getPos().y, EPS);
    }

    @Test void directionNulleNeBougePas() {
        Hero hero = nouveauHero();
        hero.setDirection(0, 0);
        hero.move();
        assertEquals(depart.x, hero.getPos().x, EPS);
        assertEquals(depart.y, hero.getPos().y, EPS);
        assertEquals("idle", hero.getAnimationState());
    }

    @Test void teleportationDefinitLaPosition() {
        Hero hero = nouveauHero();
        Point cible = TestPositionFinder.trouverCaseLibre(plateau, depart);
        hero.teleport(cible.x, cible.y);
        assertEquals(cible.x, hero.getPos().x, EPS);
        assertEquals(cible.y, hero.getPos().y, EPS);
    }

    @Test void changementDirectionChangeAnimation() {
        Hero hero = nouveauHero();
        hero.setDirection(1, 0);
        assertEquals("walk", hero.getAnimationState());
        assertEquals("right", hero.getAnimationDirection());
        hero.setDirection(0, 0);
        assertEquals("idle", hero.getAnimationState());
    }

    @Test void pointsDeVieAuDemarrage() {
        Hero hero = nouveauHero();
        assertEquals(100, hero.getMaxHp());
        assertEquals(100, hero.getHealthPoints());
        assertFalse(hero.isDead());
    }

    @Test void subirDesDegatsReduitLesPointsDeVie() {
        Hero hero = nouveauHero();
        assertTrue(hero.takeDamage(25));
        assertEquals(75, hero.getHealthPoints());
        assertEquals("hurt", hero.getAnimationState());
    }

    @Test void degatsNulsOuNegatifsSontRejetes() {
        Hero hero = nouveauHero();
        assertFalse(hero.takeDamage(0));
        assertFalse(hero.takeDamage(-5));
        assertEquals(100, hero.getHealthPoints());
    }

    @Test void invulnerabiliteBloqueDeuxDegatsImmediats() {
        Hero hero = nouveauHero();
        assertTrue(hero.takeDamage(10));
        assertFalse(hero.takeDamage(20));
        assertEquals(90, hero.getHealthPoints());
    }

    @Test void degatsMortelsNeDonnePasPointsDeVieNegatifs() {
        Hero hero = nouveauHero();
        assertTrue(hero.takeDamage(500));
        assertEquals(0, hero.getHealthPoints());
        assertTrue(hero.isDead());
        assertFalse(hero.takeDamage(1));
    }
}
