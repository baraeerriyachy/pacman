package fr.ul.acl.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Cartes temporaires sur le classpath des tests, sans changer les cartes du jeu. */
final class TestMapSupport {
    private final List<Path> created = new ArrayList<>();

    String create(String difficulty, String content) throws IOException, URISyntaxException {
        Path classpath = Paths.get(TestMapSupport.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI());
        Path folder = classpath.resolve("map").resolve(difficulty);
        Files.createDirectories(folder);
        String name = "acl_test_" + UUID.randomUUID().toString().replace("-", "") + ".txt";
        Path file = folder.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        created.add(file);
        return difficulty + "/" + name;
    }

    void clean() throws IOException {
        for (Path file : created) Files.deleteIfExists(file);
        created.clear();
    }
}

