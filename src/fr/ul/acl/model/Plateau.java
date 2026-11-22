package fr.ul.acl.model;

import java.util.*;
import java.awt.Point;

public class Plateau {
    private final int largeur;
    private final int hauteur;
    private final Block[][] grid;
    private boolean interacable;
    private final List<Point> lightSources = new ArrayList<>();

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

    public void addLightSource(int x, int y) {
        lightSources.add(new Point(x, y));
    }

    public List<Point> getLightSources() {
        return lightSources;
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
        for (int x = p.xPrev(); x <= p.xNext(); x++) {
            for (int y = p.yPrev(); y <= p.yNext(); y++) {
                if (grid[x][y].getType() == Block.Type.WALL)
                {
                    Trigger t = grid[x][y].getTrigger();
                    if(t == null)  
                        return false;
                    return t.isActivated();
                }
            }
        }

        return true;
    }

    public BlockEffects interact(Position p) {
        int damage = 0;
        Point destination = null;
        boolean exit = false;
        Trigger trigger = null;
        interacable = true;

        for (int x = p.xPrev(); x <= p.xNext(); x++) {
            for (int y = p.yPrev(); y <= p.yNext(); y++) {

                Block block = grid[x][y];

                switch (block.getType()) {

                    case TRAP:
                        damage = Math.max(damage, block.getDamage());
                        Trigger t = block.getTrigger();
                        if(t != null){
                            if(t.isActivated())
                                damage = 0;
                            else
                                t.activate();
                        }
                        interacable = false;
                        break;

                    case PORTAL:
                        if (block.getDestination() != null) {
                            destination = block.getDestination();
                        }
                        break;

                    case TRIGGER:
                        trigger = block.getTrigger();
                        interacable = !trigger.isActivated();
                        break;

                    case EXIT:
                        exit = true;
                        break;

                    default:
                        interacable = false;
                        break;
                }
            }
        }

        return new BlockEffects(damage, destination, exit, trigger);
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

    public boolean isInteracable(){
        return interacable;
    }
    
    public int getNombreTotalTresors() {
        int total = 0;

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                Trigger trigger = grid[x][y].getTrigger();

                if (trigger instanceof Tresor) {
                    total++;
                }
            }
        }

        return total;
    }
}
