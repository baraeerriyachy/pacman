package fr.ul.acl.model;

public class Hero {
    private float x;
    private float y;

    public Hero(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /** Déplace le héros de dx colonnes et dy lignes. */
    public void deplacer(float nx, float ny) {
        x =  nx;
        y = ny;
    }

    @Override
    public String toString() {
        return "Hero(" + x + "," + y + ")";
    }
}