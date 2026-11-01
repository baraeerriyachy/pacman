package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Obstacle;

public class ObstacleTest {

    @Test
    public void coordonneesStockees() {
        Obstacle o = new Obstacle(3, 7);
        assertEquals(3, o.getX());
        assertEquals(7, o.getY());
    }

    @Test
    public void obstacleALOrigine() {
        Obstacle o = new Obstacle(0, 0);
        assertEquals(0, o.getX());
        assertEquals(0, o.getY());
    }

    @Test
    public void deuxObstaclesSontIndependants() {
        Obstacle a = new Obstacle(1, 2);
        Obstacle b = new Obstacle(5, 6);
        assertEquals(1, a.getX());
        assertEquals(2, a.getY());
        assertEquals(5, b.getX());
        assertEquals(6, b.getY());
    }
}