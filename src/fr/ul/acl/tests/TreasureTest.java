package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import fr.ul.acl.model.*;
import fr.ul.acl.utility.MapReader;
import fr.ul.acl.view.VisualEffect;

/** Sprint 3 : tresors et compteur de collecte. */
class TreasureTest {
    private final TestMapSupport maps = new TestMapSupport();
    @AfterEach void clean() throws IOException { maps.clean(); }

    @Test void coffreExisteSansCollecteInitiale() {
        Plateau p = new Plateau(3, 3);
        Trigger t = new Trigger(VisualEffect.getTexture("tresor"));
        p.addTreasure(1, 1, Block.trigger(t));
        assertEquals(1, p.getNombreTotalTresors());
        assertEquals(0, p.getTresorsCollectes());
        assertSame(t, p.getBlockTrigger(1, 1));
    }

    @Test void collecterUnTresorAugmenteLeCompteur() {
        Plateau p = new Plateau(3, 3);
        Trigger t = new Trigger(VisualEffect.getTexture("tresor"));
        p.addTreasure(1, 1, Block.trigger(t));
        t.setActived();
        assertEquals(1, p.getTresorsCollectes());
    }

    @Test void lecteurChargeLesTresorsSurUneCarte() throws Exception {
        String file = maps.create("easy", """
                HR __ TS
                __ EX __
                """);
        MapReader reader = new MapReader(file);
        assertEquals(Block.Type.TRIGGER, reader.getPlateau().getBlockType(2, 0));
        assertEquals(1, reader.getPlateau().getNombreTotalTresors());
        assertEquals(0, reader.getPlateau().getTresorsCollectes());
    }
}
