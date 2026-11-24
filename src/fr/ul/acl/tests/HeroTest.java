
package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Hero;

class HeroTest {

    @Test
    void constructeurInitialiseCorrectementLeHero() {
        Hero hero = new Hero(2.0f, 3.0f);

        assertEquals(100, hero.getHealthPoints());
        assertEquals(100, hero.getMaxHp());

        assertEquals(2.0f, hero.getPos().x);
        assertEquals(3.0f, hero.getPos().y);

        assertEquals("idle", hero.getAnimationState());
        assertEquals("front", hero.getAnimationDirection());

        assertFalse(hero.isDead());
    }

    @Test
    void directionInitialeEstFront() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertEquals("front", hero.getAnimationDirection());
    }

    @Test
    void animationDirectionSuitLaDirectionDuDeplacement() {
        Hero hero = new Hero(1.0f, 1.0f);

        hero.setDirection(1, 0);
        assertEquals("right", hero.getAnimationDirection());

        hero.setDirection(-1, 0);
        assertEquals("left", hero.getAnimationDirection());

        hero.setDirection(0, 1);
        assertEquals("front", hero.getAnimationDirection());

        hero.setDirection(0, -1);
        assertEquals("back", hero.getAnimationDirection());
    }

    @Test
    void animationDirectionConserveLaDerniereDirectionQuandHeroEstImmobile() {
        Hero hero = new Hero(1.0f, 1.0f);

        hero.setDirection(1, 0);
        assertEquals("right", hero.getAnimationDirection());

        hero.setDirection(0, 0);
        assertEquals("right", hero.getAnimationDirection());
    }

    @Test
    void animationStateChangeSelonLaDirection() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertEquals("idle", hero.getAnimationState());

        hero.setDirection(1, 0);
        assertEquals("walk", hero.getAnimationState());

        hero.setDirection(0, 0);
        assertEquals("idle", hero.getAnimationState());
    }

    @Test
    void teleportDeplaceLeHero() {
        Hero hero = new Hero(1.0f, 2.0f);

        hero.teleport(5, 7);

        assertEquals(5.0f, hero.getPos().x);
        assertEquals(7.0f, hero.getPos().y);
    }

    @Test
    void recevoirDesDegatsDiminueLesPointsDeVie() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertTrue(hero.takeDamage(30));

        assertEquals(70, hero.getHealthPoints());
        assertEquals("hurt", hero.getAnimationState());
        assertFalse(hero.isDead());
    }

    @Test
    void degatsNePeuventPasDepasserLesPointsDeVieRestants() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertTrue(hero.takeDamage(150));

        assertEquals(0, hero.getHealthPoints());
        assertTrue(hero.isDead());
    }

    @Test
    void degatsInvalidesSontRefuses() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertFalse(hero.takeDamage(0));
        assertFalse(hero.takeDamage(-10));

        assertEquals(100, hero.getHealthPoints());
    }

    @Test
    void unHeroMortNePeutPlusRecevoirDeDegats() throws InterruptedException {
        Hero hero = new Hero(1.0f, 1.0f);

        assertTrue(hero.takeDamage(100));

        Thread.sleep(1100);

        assertFalse(hero.takeDamage(10));
        assertEquals(0, hero.getHealthPoints());
        assertTrue(hero.isDead());
    }

    @Test
    void heroEstInvulnerablePendantUneSecondeApresAvoirSubiDesDegats() {
        Hero hero = new Hero(1.0f, 1.0f);

        assertTrue(hero.takeDamage(20));

        assertFalse(hero.takeDamage(20));
        assertEquals(80, hero.getHealthPoints());
    }

    @Test
    void heroPeutRecevoirDesDegatsApresLaFinDeLInvulnerabilite()
            throws InterruptedException {

        Hero hero = new Hero(1.0f, 1.0f);

        assertTrue(hero.takeDamage(20));

        Thread.sleep(1100);

        assertTrue(hero.takeDamage(20));
        assertEquals(60, hero.getHealthPoints());
    }
}
