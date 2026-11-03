package fr.ul.acl.model;
import java.awt.Point;

public class Monstre {
    private float x;
    private float y;
    private Point direction;
    private static final float vitesse = 0.2f;

    public Monstre(float x, float y){
        this.x = x;
        this.y = y;
        this.direction = new Point(0,0)
    }

    public setDirection(int dx, int dy){
        direction.x = dx;
        direction.y = dy;
    }

    public float[2] nextStep() {
        return {x + direction.x * vitesse, y + direction.y * vitesse}
    }

    public void inverseDirection(){
        direction.x *= -1;
        direction.y *= -1;
    }
}