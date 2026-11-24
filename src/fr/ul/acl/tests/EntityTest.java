
package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Entity;
import fr.ul.acl.model.Position;

class EntityTest {

    private static class TestEntity extends Entity {

        TestEntity(float x, float y) {
            super(x, y);
        }

        TestEntity(float x, float y, float vitesse) {
            super(x, y, vitesse);
        }
    }

    @Test
    void constructeurInitialiseCorrectementLEntite() {
        Entity entity = new TestEntity(2.0f, 3.0f);

        assertEquals(2.0f, entity.getPos().x);
        assertEquals(3.0f, entity.getPos().y);
        assertEquals(2.0f, entity.getNewPos().x);
        assertEquals(3.0f, entity.getNewPos().y);
    }

    @Test
    void constructeurAvecVitessePersonnalisee() {
        Entity entity = new TestEntity(2.0f, 3.0f, 0.5f);

        entity.setDirection(1, 0);

        assertEquals(2.5f, entity.getNewPos().x);
        assertEquals(3.0f, entity.getNewPos().y);
    }

    @Test
    void setDirectionModifieLaDirectionDuDeplacement() {
        Entity entity = new TestEntity(1.0f, 1.0f);

        entity.setDirection(0, 1);

        assertEquals(1.0f, entity.getNewPos().x);
        assertEquals(1.2f, entity.getNewPos().y);
    }

    @Test
    void getNewPosCalculeLaNouvellePositionSansDeplacerLEntite() {
        Entity entity = new TestEntity(1.0f, 1.0f);

        entity.setDirection(1, 0);

        Position newPos = entity.getNewPos();

        assertEquals(1.2f, newPos.x);
        assertEquals(1.0f, newPos.y);

        assertEquals(1.0f, entity.getPos().x);
        assertEquals(1.0f, entity.getPos().y);
    }

    @Test
    void getNewPosArronditLesCoordonneesADeuxDecimales() {
        Entity entity = new TestEntity(1.0f, 1.0f, 0.12345f);

        entity.setDirection(1, 1);

        Position newPos = entity.getNewPos();

        assertEquals(1.12f, newPos.x);
        assertEquals(1.12f, newPos.y);
    }

    @Test
    void moveMetAJourLaPositionDeLEntite() {
        Entity entity = new TestEntity(1.0f, 1.0f);

        entity.setDirection(1, 0);
        entity.getNewPos();
        entity.move();

        assertEquals(1.2f, entity.getPos().x);
        assertEquals(1.0f, entity.getPos().y);
    }

    @Test
    void moveSansDirectionNeChangePasLaPosition() {
        Entity entity = new TestEntity(1.0f, 1.0f);

        entity.move();

        assertEquals(1.0f, entity.getPos().x);
        assertEquals(1.0f, entity.getPos().y);
    }

    @Test
    void plusieursDeplacementsSontCumules() {
        Entity entity = new TestEntity(1.0f, 1.0f);

        entity.setDirection(1, 0);

        entity.move();
        entity.move();
        entity.move();

        assertEquals(1.6f, entity.getPos().x);
        assertEquals(1.0f, entity.getPos().y);
    }
}
