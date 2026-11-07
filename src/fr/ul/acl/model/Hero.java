package fr.ul.acl.model;

public class Hero extends Entity {

    private static final int MAX_HP = 100;
    private static final long INVULNERABILITY_DURATION = 1000; // 1 second

    private int healthPoints;
    private long lastDamageTime;

    public Hero(float x, float y) {
        super(x, y);
        this.healthPoints = MAX_HP;
        this.lastDamageTime = 0;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHp() {
        return MAX_HP;
    }

    public void teleport(int x, int y) {
        pos.set(x, y);
    }

    public boolean takeDamage(int damage) {
        long currentTime = System.currentTimeMillis();

        // Check whether the hero is still invulnerable
        if (currentTime - lastDamageTime < INVULNERABILITY_DURATION) {
            return false;
        }

        // Reject invalid damage or damage to a dead hero
        if (damage <= 0 || healthPoints <= 0) {
            return false;
        }

        healthPoints = Math.max(0, healthPoints - damage);
        lastDamageTime = currentTime;

        return true;
    }

    public boolean isDead() {
        return healthPoints == 0;
    }
}