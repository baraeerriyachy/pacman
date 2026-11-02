package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.GameLevel;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.MapReader;
import fr.ul.acl.model.Plateau;

class GameLevelTest {

    @Test
    void troisNiveauxDeDifficulteSontDisponibles() {
        assertEquals(3, GameLevel.getAvailableLevels().size());
        assertEquals("Facile", GameLevel.getAvailableLevels().get(0).getDifficulty());
        assertEquals("Moyen", GameLevel.getAvailableLevels().get(1).getDifficulty());
        assertEquals("Difficile", GameLevel.getAvailableLevels().get(2).getDifficulty());
    }

    @Test
    void chaqueNiveauChargeUneCarteValideEtAccessible() throws Exception {
        for (GameLevel level : GameLevel.getAvailableLevels()) {
            MapReader reader = new MapReader(level.getMapFile());
            Plateau plateau = reader.getPlateau();
            Point start = reader.getDepart();
            Point exit = plateau.getSortie();

            assertNotNull(start, level.getName());
            assertNotNull(exit, level.getName());
            assertFalse(plateau.estObstacle(start.x, start.y));
            assertFalse(plateau.estObstacle(exit.x, exit.y));
            assertTrue(sortieAccessible(plateau, start, exit),
                    "La sortie doit etre accessible dans " + level.getName());
        }
    }

    @Test
    void leJeuChargeLeNiveauSelectionneEtLeConserveAuRedemarrage() throws Exception {
        GameLevel level = GameLevel.getAvailableLevels().get(1);
        HachimiGame game = new HachimiGame("fichier_aide_absent.txt", level);

        assertSame(level, game.getLevel());
        assertEquals(12, game.getPlateau().getLargeur());
        assertEquals(10, game.getPlateau().getHauteur());

        game.recommencer();

        assertSame(level, game.getLevel());
        assertEquals(12, game.getPlateau().getLargeur());
        assertEquals(10, game.getPlateau().getHauteur());
        assertFalse(game.isFinished());
        assertEquals(0L, game.getElapsedSeconds());
    }

    private boolean sortieAccessible(Plateau plateau, Point start, Point exit) {
        Queue<Point> pending = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();
        pending.add(start);
        visited.add(start);
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!pending.isEmpty()) {
            Point current = pending.remove();
            if (current.equals(exit)) {
                return true;
            }
            for (int[] direction : directions) {
                Point next = new Point(current.x + direction[0], current.y + direction[1]);
                if (plateau.estDansLimites(next.x, next.y)
                        && !plateau.estObstacle(next.x, next.y)
                        && visited.add(next)) {
                    pending.add(next);
                }
            }
        }
        return false;
    }
}
