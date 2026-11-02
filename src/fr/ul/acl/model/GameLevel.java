package fr.ul.acl.model;

import java.util.List;

/**
 * Description d'un niveau jouable. Les cartes sont chargees depuis les
 * ressources du jeu au format texte utilise par MapReader.
 */
public final class GameLevel {

    private static final List<GameLevel> LEVELS = List.of(
        new GameLevel("Niveau 1 - Facile", "Facile", "map.txt"),
        new GameLevel("Niveau 2 - Moyen", "Moyen", "level2.txt"),
        new GameLevel("Niveau 3 - Difficile", "Difficile", "level3.txt")
    );

    private final String name;
    private final String difficulty;
    private final String mapFile;

    public GameLevel(String name, String difficulty, String mapFile) {
        if (name == null || name.isBlank()
                || difficulty == null || difficulty.isBlank()
                || mapFile == null || mapFile.isBlank()) {
            throw new IllegalArgumentException("Les informations du niveau sont obligatoires.");
        }
        this.name = name;
        this.difficulty = difficulty;
        this.mapFile = mapFile;
    }

    public static List<GameLevel> getAvailableLevels() {
        return LEVELS;
    }

    public static GameLevel getDefaultLevel() {
        return LEVELS.get(0);
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getMapFile() {
        return mapFile;
    }

    @Override
    public String toString() {
        return name;
    }
}
