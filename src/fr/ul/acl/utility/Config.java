package fr.ul.acl.utility;
import java.util.Map;

public final class Config {
    private Config() {}

    public static final Map<String, Map<String, Integer>> DAMAGE = Map.of(
        "ghost", Map.of(
            "easy", 5,
            "moderate", 10,
            "difficult", 15
        ),
        "zombie", Map.of(
            "easy", 10,
            "moderate", 20,
            "difficult", 30
        ),
        "trap", Map.of(
            "easy", 10,
            "moderate", 15,
            "difficult", 20
        )
    );

    public static final Map<String, Map<String, Float>> SPEED = Map.of(
        "ghost", Map.of(
            "easy", 0.15f,
            "moderate", 0.2f,
            "difficult", 0.3f
        ),
        "zombie", Map.of(
            "easy", 0.02f,
            "moderate", 0.03f,
            "difficult", 0.05f
        )
    );
}