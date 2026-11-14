package fr.ul.acl.utility;
import java.util.Map;

public final class Config {
    private Config() {}

    public static final Map<String, Map<String, Integer>> DAMAGE = Map.of(
        "ghost", Map.of(
            "easy", 5,
            "medium", 10,
            "hard", 15
        ),
        "zombie", Map.of(
            "easy", 10,
            "medium", 20,
            "hard", 30
        ),
        "trap", Map.of(
            "easy", 10,
            "medium", 15,
            "hard", 20
        )
    );

    public static final Map<String, Map<String, Float>> SPEED = Map.of(
        "ghost", Map.of(
            "easy", 0.15f,
            "medium", 0.2f,
            "hard", 0.3f
        ),
        "zombie", Map.of(
            "easy", 0.02f,
            "medium", 0.03f,
            "hard", 0.05f
        )
    );
}