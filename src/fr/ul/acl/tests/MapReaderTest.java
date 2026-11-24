package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.Block;
import fr.ul.acl.model.Plateau;
import fr.ul.acl.utility.MapReader;

/** Lecture des fichiers de carte selon le format actuel: HR, EX, WL, __. */
class MapReaderTest {
    private final TestMapSupport maps = new TestMapSupport();

    @AfterEach void clean() throws IOException { maps.clean(); }

    @Test void lectureDUneCarteReelle() throws Exception {
        MapReader reader = new MapReader("easy/level1.txt");
        Plateau p = reader.getPlateau();
        assertNotNull(reader.getDepart());
        assertTrue(p.getLargeur() > 0);
        assertTrue(p.getHauteur() > 0);
        assertEquals(Block.Type.WALL, p.getBlockType(2, 0));
    }

    @Test void formatCarteSimpleEtCoordonnees() throws Exception {
        String file = maps.create("easy", "HR __ WL\n__ EX __\n");
        MapReader reader = new MapReader(file);
        assertEquals(3, reader.getPlateau().getLargeur());
        assertEquals(2, reader.getPlateau().getHauteur());
        assertEquals(new Point(0, 0), reader.getDepart());
        assertEquals(Block.Type.EMPTY, reader.getPlateau().getBlockType(1, 0));
        assertEquals(Block.Type.WALL, reader.getPlateau().getBlockType(2, 0));
        assertEquals(Block.Type.EXIT, reader.getPlateau().getBlockType(1, 1));
    }

    @Test void donneesDuPlateauNonTransposees() throws Exception {
        String file = maps.create("easy", "__ __ WL\nHR EX __\n");
        Plateau p = new MapReader(file).getPlateau();
        assertEquals(Block.Type.WALL, p.getBlockType(2, 0));
        assertEquals(Block.Type.EMPTY, p.getBlockType(0, 0));
    }

    @Test void lignesVidesIgnorees() throws Exception {
        String file = maps.create("easy", "\nHR EX\n\n__ __\n");
        assertEquals(2, new MapReader(file).getPlateau().getHauteur());
    }

    @Test void fichierInexistantDeclencheIOException() {
        assertThrows(IOException.class,
                () -> new MapReader("easy/carte_introuvable_acl.txt"));
    }

    @Test void carteVideDeclencheIOException() throws Exception {
        String file = maps.create("easy", "\n\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void lignesDeLongueursDifferentesSontRefusees() throws Exception {
        String file = maps.create("easy", "HR __ EX\n__ __\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void symboleMalFormeEstRefuse() throws Exception {
        String file = maps.create("easy", "HR !!\n__ EX\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void symboleInconnuEstRefuse() throws Exception {
        String file = maps.create("easy", "HR XX\n__ EX\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void plusieursDepartsSontRefuses() throws Exception {
        String file = maps.create("easy", "HR HR\n__ EX\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void departManquantEstRefuse() throws Exception {
        String file = maps.create("easy", "__ __\n__ EX\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }

    @Test void identifiantRequisPourLevier() throws Exception {
        String file = maps.create("easy", "HR LV\n__ EX\n");
        assertThrows(IOException.class, () -> new MapReader(file));
    }
}
