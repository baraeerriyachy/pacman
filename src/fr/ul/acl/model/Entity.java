package fr.ul.acl.model;
import java.awt.Point;

public abstract class Entity {
    protected Position pos;
    protected Point direction;
    protected float vitesse;
    private Position nPos;

    public Entity(float x, float y) {
        this(x, y, 0.2f);
    }
    
    public Entity(float x, float y, float vitesse){
        this.pos = new Position(x,y);
        this.direction = new Point(0,0);
        this.vitesse = vitesse;
        this.nPos = pos;
    }

    public void setDirection(int dx, int dy){
        direction.x = dx;
        direction.y = dy;
    }

    public Position getNewPos() {
        this.nPos = new Position(
            Math.round((pos.x + direction.x * vitesse) * 100f) / 100f, 
            Math.round((pos.y + direction.y * vitesse) * 100f) / 100f
        );
        return nPos;
    }

    public void move(){
        if(nPos.x == pos.x && nPos.y == pos.y)
            getNewPos();
        pos = nPos;
    }

    public Position getPos(){
        return pos;
    }
}