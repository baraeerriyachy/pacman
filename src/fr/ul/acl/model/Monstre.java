package fr.ul.acl.model;

public class Monstre extends Entity {

    private final int damage;
    private int maxMove;
    private float moveCount;
    private boolean intangible = false;

    public Monstre(float x, float y, int damage, float speed) {
        this(x,y,damage,speed, false);
    }

    public Monstre(float x, float y, int damage, float speed, boolean intangible) {
        super(x, y, speed);
        this.damage = damage;
        this.moveCount = 0;
        this.intangible = intangible;
    }

    public static Monstre zombie(float x, float y) {
        return new Monstre(x, y, 20, 0.03f);
    }

    public static Monstre ghost(float x, float y) {
        return new Monstre(x, y, 10, 0.25f, true);
    }

    public boolean isIntangible() {
        return intangible;
    }

    public int getDamage() {
        return damage;
    }

    public void setMaxMove(int max_move) {
        this.maxMove = max_move;
    }

    private void inverseDirection() {
        direction.x *= -1;
        direction.y *= -1;
    }

    @Override
    public void move(){
        super.move();
        moveCount+=vitesse;

        if(moveCount >= maxMove)
        {
            moveCount = 0;
            inverseDirection();
        }
    }

}