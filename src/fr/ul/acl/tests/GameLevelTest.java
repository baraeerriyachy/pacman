
package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.GameLevel;

class GameLevelTest {

    @Test
    void constructeurInitialiseCorrectementLeNiveau() {
        GameLevel level = new GameLevel(
                "Test Level", 2, "moderate/level1.txt");

        assertEquals("Test Level", level.getName());
        assertEquals(2, level.getDifficulty());
        assertEquals("moderate/level1.txt", level.getMapFile());

        assertTrue(level.isLocked());
        assertFalse(level.isDark());
        assertEquals(0, level.getStars());
    }

    @Test
    void constructeurRefuseLesInformationsInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel(null, 1, "map.txt"));

        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel("", 1, "map.txt"));

        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel("Test", -1, "map.txt"));

        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel("Test", 4, "map.txt"));

        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel("Test", 1, null));

        assertThrows(IllegalArgumentException.class,
                () -> new GameLevel("Test", 1, ""));
    }

    @Test
    void debloquerUnNiveauLeRendAccessible() {
        GameLevel level = new GameLevel(
                "Test", 2, "moderate/level1.txt");

        assertTrue(level.isLocked());

        level.unlock();

        assertFalse(level.isLocked());
    }

    @Test
    void debloquerUnNiveauMetAJourLaDerniereDifficulte() {
        GameLevel level = new GameLevel(
                "Test", 3, "difficult/level1.txt");

        level.unlock();

        assertEquals(3, GameLevel.LatestDifficulty);
    }

    @Test
    void lesEtoilesRepresententLeMeilleurScoreObtenu() {
        GameLevel level = new GameLevel(
                "Test", 1, "easy/level1.txt");

        level.setStars(2);
        assertEquals(2, level.getStars());

        level.setStars(1);
        assertEquals(2, level.getStars());

        level.setStars(3);
        assertEquals(3, level.getStars());

        level.setStars(0);
        assertEquals(3, level.getStars());
    }

    @Test
    void nombreDEtoilesDoitEtreEntreZeroEtTrois() {
        GameLevel level = new GameLevel(
                "Test", 1, "easy/level1.txt");

        assertThrows(IllegalArgumentException.class,
                () -> level.setStars(-1));

        assertThrows(IllegalArgumentException.class,
                () -> level.setStars(4));
    }

    @Test
    void activerObscuriteModifieSonEtat() {
        GameLevel level = new GameLevel(
                "Test", 1, "easy/level1.txt");

        assertFalse(level.isDark());

        level.setDark();

        assertTrue(level.isDark());
    }
}
