package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.GameLevel;
import fr.ul.acl.model.HachimiGame;

/** Tests de la gestion des niveaux, y compris un controle des ressources manquantes. */
class GameLevelTest {

    @Test void neufNiveauxSontAnnonces() {
        assertEquals(9, GameLevel.getAvailableLevels().size());
    }

    @Test void troisDifficultesSontAnnonces() {
        assertArrayEquals(new String[] {"easy", "moderate", "difficult"}, GameLevel.DIFFICULTIES);
    }

    @Test void informationsDuPremierNiveau() {
        GameLevel level = GameLevel.getAvailableLevels().get(0);
        assertEquals("Level 1 - easy", level.getName());
        assertEquals(1, level.getDifficulty());
        assertEquals("easy/level1.txt", level.getMapFile());
    }

    @Test void niveauNeufEstDifficile() {
        GameLevel level = GameLevel.getAvailableLevels().get(8);
        assertEquals(3, level.getDifficulty());
        assertEquals("difficult/level3.txt", level.getMapFile());
    }

    @Test void niveauCreeEstVerrouille() {
        GameLevel level = new GameLevel("Test", 1, "easy/level1.txt");
        assertTrue(level.isLocked());
        level.unlock();
        assertFalse(level.isLocked());
    }

    @Test void creationAvecArgumentsInvalides() {
        assertThrows(IllegalArgumentException.class, () -> new GameLevel(null, 1, "easy/level1.txt"));
        assertThrows(IllegalArgumentException.class, () -> new GameLevel(" ", 1, "easy/level1.txt"));
        assertThrows(IllegalArgumentException.class, () -> new GameLevel("Test", -1, "easy/level1.txt"));
        assertThrows(IllegalArgumentException.class, () -> new GameLevel("Test", 4, "easy/level1.txt"));
        assertThrows(IllegalArgumentException.class, () -> new GameLevel("Test", 1, ""));
    }

    @Test void etoilesInitialesEtLimites() {
        GameLevel level = new GameLevel("Test", 1, "easy/level1.txt");
        assertEquals(0, level.getStars());
        level.setStars(3);
        assertEquals(3, level.getStars());
        assertThrows(IllegalArgumentException.class, () -> level.setStars(-1));
        assertThrows(IllegalArgumentException.class, () -> level.setStars(4));
    }

    @Test void meilleurScoreEstConserve() {
        GameLevel level = new GameLevel("Test", 1, "easy/level1.txt");
        level.setStars(3);
        level.setStars(1);
        assertEquals(3, level.getStars());
    }

    @Test void modeSombre() {
        GameLevel level = new GameLevel("Test", 1, "easy/level1.txt");
        assertFalse(level.isDark());
        level.setDark();
        assertTrue(level.isDark());
    }

    @Test void partieChargeLeNiveauEtPeutRedemarrer() throws Exception {
        GameLevel level = new GameLevel("Test", 1, "easy/level1.txt");
        HachimiGame game = new HachimiGame(level);
        assertSame(level, game.getLevel());
        assertNotNull(game.getPlateau());
        assertFalse(game.isFinished());
        game.recommencer();
        assertSame(level, game.getLevel());
        assertFalse(game.isFinished());
    }

    @Test void ressourcesDesNiveauxAnnonceesSontDisponibles() {
        // Si un fichier est absent, lister tous les niveaux incomplets.
        java.util.List<String> manquantes = new java.util.ArrayList<>();
        for (GameLevel level : GameLevel.getAvailableLevels()) {
            String path = "/map/" + level.getMapFile();
            try (InputStream stream = GameLevelTest.class.getResourceAsStream(path)) {
                if (stream == null) manquantes.add(path);
            } catch (java.io.IOException e) {
                fail("Impossible de verifier " + path + " : " + e.getMessage());
            }
        }
        assertTrue(manquantes.isEmpty(), "Cartes manquantes : " + manquantes);
    }

}
