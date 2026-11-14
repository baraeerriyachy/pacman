
package fr.ul.acl.model;

import java.util.List;
import java.util.ArrayList;

public final class GameLevel {

    private static final List<GameLevel> LEVELS = new ArrayList<>();
    public static String[] DIFFICULTIES = {
            "easy", "moderate", "difficult"
        };
    
    static {
        for (int difficulty = 1; difficulty <= 3; difficulty++) {
            for (int level = 1; level <= 3; level++) {
                LEVELS.add(new GameLevel(
                    "Level " + level + " - " + DIFFICULTIES[difficulty-1],
                    difficulty,
                    DIFFICULTIES[difficulty-1] + "/level" + level + ".txt"
                ));
            }
        }
        for (int level = 4; level <= 12; level++) {
            LEVELS.add(new GameLevel(
                "Level " + level + " - " + DIFFICULTIES[2],
                3,
                DIFFICULTIES[2] + "/level0.txt"
            ));
        }
    }

    private final String name;
    private final int difficulty;
    private final String mapFile;
    private boolean locked;

    public GameLevel(String name, int difficulty, String mapFile) {
        if (name == null || name.isBlank()
                || difficulty < 0 || difficulty > 3
                || mapFile == null || mapFile.isBlank()) {
            throw new IllegalArgumentException("Informations du niveau invalides.");
        }

        this.name = name;
        this.difficulty = difficulty;
        this.mapFile = mapFile;
        this.locked = false;
    }

    // Queries
    public static List<GameLevel> getAvailableLevels() {
        return LEVELS;
    }


    // Getters
    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getMapFile() {
        return mapFile;
    }

    public boolean isLocked() {
        return locked;
    }

    // Actions

    public void unlock() {
        locked = false;
    }
}
