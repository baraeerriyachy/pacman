package fr.ul.acl.model;

public class Hero {
    private float x;
    private float y;
    private static final float vitesse = 0.2f;

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

    public float getVitesse() {
        return vitesse;
    }

    /** Déplace le héros de dx colonnes et dy lignes. */
    public void deplacer(float nx, float ny) {
        x =  nx;
        y = ny;
    }
}