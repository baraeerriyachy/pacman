package fr.ul.acl.model;

public class Position {
    public float x;
    public float y;
    private static final float MARGIN = 0.2f;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int xNext(){
        return (int) Math.ceil(x - MARGIN);
    }

    public int yNext(){
        return (int) Math.ceil(y - MARGIN);
    }

    public int xPrev(){
        return (int) (x + MARGIN);
    }

    public int yPrev(){
        return (int) (y + MARGIN);
    }

    public boolean isOverlapping(Position p) {
        return x + MARGIN < p.x + 1 &&
            x + 1 - MARGIN > p.x &&
            y + MARGIN < p.y + 1 &&
            y + 1 - MARGIN > p.y;
    }
}