
package fr.ul.acl.model;

import java.util.List;
import java.util.ArrayList;

public final class GameLevel {

    private static final List<GameLevel> LEVELS = new ArrayList<>();
    public static int LatestDifficulty;

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
        for(int i = 0; i < 6; i++){
            LEVELS.get(i).unlock();
        }
        LEVELS.get(5).setDark();
    }

    private final String name;
    private final int difficulty;
    private final String mapFile;
    private boolean darkness;
    private boolean locked;
    private int stars;

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
        this.darkness = false;
        this.stars = 0;
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

    public int getStars() {
        return stars;
    }

    // Actions
    public void unlock() {
        locked = false;
        LatestDifficulty = this.difficulty;
    }
    

    public void setStars(int stars) {
        if (stars < 0 || stars > 3) {
            throw new IllegalArgumentException("Stars must be between 0 and 3.");
        }

        this.stars = Math.max(this.stars, stars);
    }

    // Actions
    public void setDark() {
        darkness = true;
    }

    // Actions
    public boolean isDark() {
        return darkness;
    }
}
