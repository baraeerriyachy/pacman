package fr.ul.acl.model;

public class Hero {
    private int x;
    private int y;

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /** Déplace le héros de dx colonnes et dy lignes. */
    public void deplacer(int dx, int dy) {
        x += dx;
        y += dy;
    }

    @Override
    public String toString() {
        return "Hero(" + x + "," + y + ")";
    }
}