package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.ul.acl.model.MapReader;
import fr.ul.acl.model.Plateau;

/**
 * Tests de MapReader.
 *
 * MapReader lit la carte comme une RESSOURCE du classpath ("/" + nom).
 * Pour tester des cartes inventees (valides ou invalides), chaque test ecrit
 * un petit fichier dans le dossier des classes de test (target/test-classes),
 * qui est dans le classpath, puis le supprime a la fin.
 * La vraie carte "map.txt" n'est testee que sur des proprietes qui doivent
 * toujours etre vraies, pour ne pas casser si le groupe modifie la carte.
 */
public class MapReaderTest {

    private final List<Path> fichiersTemporaires = new ArrayList<>();

    /** Ecrit une carte temporaire et retourne son nom (pour MapReader). */
    private String creerCarte(String nom, String contenu) throws Exception {
        Path dossier = Paths.get(MapReaderTest.class
                .getProtectionDomain().getCodeSource().getLocation().toURI());
        String nomFichier = "tmp_" + nom + ".txt";
        Path fichier = dossier.resolve(nomFichier);
        Files.write(fichier, contenu.getBytes(StandardCharsets.UTF_8));
        fichiersTemporaires.add(fichier);
        return nomFichier;
    }

    @AfterEach
    public void nettoyer() throws IOException {
        for (Path f : fichiersTemporaires) {
            Files.deleteIfExists(f);
        }
        fichiersTemporaires.clear();
    }

    // ---------- la vraie carte du jeu ----------

    @Test
    public void carteParDefautEstCoherente() throws Exception {
        MapReader lecteur = new MapReader("map.txt");
        Plateau p = lecteur.getPlateau();
        Point depart = lecteur.getDepart();

        assertNotNull(p);
        assertNotNull(depart);
        assertNotNull(p.getSortie());
        Point sortie = p.getSortie();

        assertTrue(p.getLargeur() > 0);
        assertTrue(p.getHauteur() > 0);
        assertTrue(p.estDansLimites(depart.x, depart.y));
        assertTrue(p.estDansLimites(sortie.x, sortie.y));
        assertFalse(depart.equals(sortie));
        assertFalse(p.estObstacle(depart.x, depart.y));
        assertFalse(p.estObstacle(sortie.x, sortie.y));
    }

    // ---------- cartes valides ----------

    @Test
    public void lectureDUneCarteValide() throws Exception {
        // 5 colonnes x 3 lignes ; H en (0,0), S en (1,1), obstacle en (4,0)
        String nom = creerCarte("valide", "H0001\n0S000\n00000\n");
        MapReader lecteur = new MapReader(nom);
        Plateau p = lecteur.getPlateau();

        assertEquals(5, p.getLargeur());
        assertEquals(3, p.getHauteur());
        assertEquals(new Point(0, 0), lecteur.getDepart());
        assertEquals(new Point(1, 1), p.getSortie());
        assertTrue(p.estObstacle(4, 0));
    }

    @Test
    public void departEtSortieNeSontPasDesObstacles() throws Exception {
        String nom = creerCarte("hs", "H0001\n0S000\n00000\n");
        Plateau p = new MapReader(nom).getPlateau();
        assertFalse(p.estObstacle(0, 0));
        assertFalse(p.estObstacle(1, 1));
        assertFalse(p.estObstacle(1, 0));
        assertFalse(p.estObstacle(0, 1));
    }

    @Test
    public void lesCoordonneesNeSontPasInversees() throws Exception {
        // x = colonne, y = ligne : la sortie est en colonne 4, ligne 2
        String nom = creerCarte("coord", "H0000\n00000\n0000S\n");
        MapReader lecteur = new MapReader(nom);
        assertEquals(new Point(4, 2), lecteur.getPlateau().getSortie());
        assertEquals(5, lecteur.getPlateau().getLargeur());
        assertEquals(3, lecteur.getPlateau().getHauteur());
    }

    @Test
    public void carteMinimale() throws Exception {
        String nom = creerCarte("mini", "HS\n");
        MapReader lecteur = new MapReader(nom);
        assertEquals(2, lecteur.getPlateau().getLargeur());
        assertEquals(1, lecteur.getPlateau().getHauteur());
        assertEquals(new Point(0, 0), lecteur.getDepart());
        assertEquals(new Point(1, 0), lecteur.getPlateau().getSortie());
    }

    @Test
    public void carteAvecPlusieursObstacles() throws Exception {
        String nom = creerCarte("obst", "H11\n101\n11S\n");
        Plateau p = new MapReader(nom).getPlateau();
        assertTrue(p.estObstacle(1, 0));
        assertTrue(p.estObstacle(2, 0));
        assertTrue(p.estObstacle(0, 1));
        assertFalse(p.estObstacle(1, 1));
        assertTrue(p.estObstacle(2, 1));
        assertTrue(p.estObstacle(0, 2));
        assertTrue(p.estObstacle(1, 2));
    }

    // ---------- cartes invalides ----------

    @Test
    public void fichierIntrouvable() {
        assertThrows(IOException.class,
                () -> new MapReader("carte_qui_n_existe_pas.txt"));
    }

    @Test
    public void carteVide() throws Exception {
        String nom = creerCarte("vide", "");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void lignesDeLongueursDifferentes() throws Exception {
        String nom = creerCarte("inegale", "H0S\n00\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void symboleInvalide() throws Exception {
        String nom = creerCarte("symbole", "H0\nXS\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void plusieursDeparts() throws Exception {
        String nom = creerCarte("deuxh", "HH\n0S\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void plusieursSorties() throws Exception {
        String nom = creerCarte("deuxs", "HS\n0S\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void departManquant() throws Exception {
        String nom = creerCarte("sansh", "00\n0S\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }

    @Test
    public void sortieManquante() throws Exception {
        String nom = creerCarte("sanss", "H0\n00\n");
        assertThrows(IOException.class, () -> new MapReader(nom));
    }
}