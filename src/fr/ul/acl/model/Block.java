package fr.ul.acl.model;

import java.awt.Point;

public class Block {

    public enum Type {
        WALL, TRAP, PORTAL, EMPTY
    }

    private final Type type;
    private final Point destination;
    private final int damage;

    private Block(Type type, Point destination, int damage) {
        this.type = type;
        this.destination = destination;
        this.damage = damage;
    }

    public static final Block EMPTY =
        new Block(Type.EMPTY, null, 0);

    public static Block wall() {
        return new Block(Type.WALL, null, 0);
    }

    public static Block trap(int damage) {
        return new Block(Type.TRAP, null, damage);
    }

    public static Block portal(Point destination) {
        return new Block(Type.PORTAL, destination, 0);
    }

    public Type getType() {
        return type;
    }

    public Point getDestination() {
        return destination;
    }

    public int getDamage() {
        return damage;
    }
}