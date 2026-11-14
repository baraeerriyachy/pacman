
package fr.ul.acl.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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
        this.locked = true;
    }

    // Queries
    public static List<GameLevel> getAvailableLevels() {
        return LEVELS;
    }

    public static GameLevel getDefaultLevel() {
        return LEVELS.get(0);
    }

    public static List<GameLevel> getAllLevels() {
        return LEVELS;
    }

    public static Optional<GameLevel> findByDifficulty(int difficulty) {
        return LEVELS.stream()
                .filter(level -> level.difficulty == difficulty)
                .findFirst();
    }

    public static List<GameLevel> getUnlockedLevels() {
        return LEVELS.stream()
                .filter(level -> !level.locked)
                .toList();
    }

    public static List<GameLevel> getLockedLevels() {
        return LEVELS.stream()
                .filter(GameLevel::isLocked)
                .toList();
    }

    public static List<GameLevel> findByDifficultyAndLockStatus(
            int difficulty, boolean locked) {
        return LEVELS.stream()
                .filter(level -> level.difficulty == difficulty)
                .filter(level -> level.locked == locked)
                .toList();
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

    @Override
    public String toString() {
        return name;
    }
}
