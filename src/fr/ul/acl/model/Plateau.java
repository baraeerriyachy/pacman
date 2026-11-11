package fr.ul.acl.model;

import java.util.*;
import java.awt.Point;

public class Plateau {
    public record BlockEffects(int damage, Point destination, boolean exit) {}
	
    private final int largeur;
    private final int hauteur;
    private final Block[][] grid;

    public Plateau(int largeur, int hauteur) {
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException(
                "Les dimensions doivent être positives."
            );
        }

        this.largeur = largeur;
        this.hauteur = hauteur;
        this.grid = new Block[largeur][hauteur];

        // Initialise every cell as EMPTY
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                grid[x][y] = Block.EMPTY;
            }
        }
        
    } 

    public boolean isInsideMap(Position p) {
        return p.x >= 0 && p.x + 1 <= largeur
        && p.y >= 0 && p.y + 1 <= hauteur;
    }

    public Block.Type getBlockType(int x, int y){
        return grid[x][y].getType();
    }

    public Trigger getBlockTrigger(int x, int y){
        return grid[x][y].getTrigger();
    }

    public boolean isFree(Position p) {
        if(!isInsideMap(p))
            return false;

        for (int x = p.xPrev(); x <= p.xNext(); x++) {
            for (int y = p.yPrev(); y <= p.yNext(); y++) {
                if (grid[x][y].getType() == Block.Type.WALL)
                    return false;
            }
        }

        return true;
    }

    public BlockEffects interact(Position p) {
        int damage = 0;
        Point destination = null;
        boolean exit = false;

        for (int x = p.xPrev(); x <= p.xNext(); x++) {
            for (int y = p.yPrev(); y <= p.yNext(); y++) {

                Block block = grid[x][y];

                switch (block.getType()) {

                    case TRAP:
                        damage = Math.max(damage, block.getDamage());
                        break;

                    case PORTAL:
                        if (block.getDestination() != null) {
                            destination = block.getDestination();
                        }
                        break;

                    case TRIGGER:
                        block.getTrigger().activate();
                        break;

                    case EXIT:
                        exit = true;
                        break;

                    default:
                        break;
                }
            }
        }

        return new BlockEffects(damage, destination, exit);
    }

    public void setBlock(int x, int y, Block block) {
        grid[x][y] = block;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }
}
