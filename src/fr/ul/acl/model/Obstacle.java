package fr.ul.acl.model;

public class Obstacle {

    private final int x;
    private final int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean estEnPosition(int x, int y) {
        return this.x == x && this.y == y;
    }
}
