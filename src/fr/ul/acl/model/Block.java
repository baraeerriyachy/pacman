package fr.ul.acl.model;

import java.awt.Point;

public class Block {

    public enum Type {
        WALL, TRAP, PORTAL, EMPTY, EXIT, TRIGGER
    }

    private final Type type;
    private final BlockEffects effect;

    private Block(Type type, Point destination, int damage, Trigger trigger) {
        this.type = type;
        this.effect = new BlockEffects(damage, destination, false, trigger);
    }

    public static final Block EMPTY =
        new Block(Type.EMPTY, null, 0, null);

    public static Block wall() {
        return new Block(Type.WALL, null, 0, null);
    }

    public static Block exit() {
        return new Block(Type.EXIT, null, 0, null);
    }

    public static Block trap(int damage) {
        return new Block(Type.TRAP, null, damage, null);
    }

    public static Block portal(Point destination) {
        return new Block(Type.PORTAL, destination, 0, null);
    }

    public static Block trigger(Trigger trigger) {
        return new Block(Type.TRIGGER, null, 0, trigger);
    }

    public Type getType() {
        return type;
    }

    public Point getDestination() {
        return effect.destination();
    }

    public int getDamage() {
        return effect.damage();
    }

    public Trigger getTrigger(){
        return effect.trigger();
    }
}