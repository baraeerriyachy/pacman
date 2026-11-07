package fr.ul.acl.model;

import java.util.*;
import java.awt.Point;

public class Plateau {

    private final int largeur;
    private final int hauteur;
    private Point sortie;
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

    public boolean isFree(Position p) {
        if(!isInsideMap(p))
        {
            // System.out.println(
			// 	"Déplacement impossible : hors limites."
			// );
            return false;
		}

        for (int x = p.xPrev(); x <= p.xNext(); x++) {
            for (int y = p.yPrev(); y <= p.yNext(); y++) {
                if (grid[x][y].getType() == Block.Type.WALL) {
                    // System.out.println(
                    //     "Déplacement impossible : obstacle."
                    // );
                    return false;
                }
            }
        }

        return true;
    }

    public Point portalActive(Position p){
        Point destination = grid[p.xNext()][p.yNext()].getDestination();
        if (destination == null) {
            destination = grid[p.xPrev()][p.yPrev()].getDestination();
        }
        return destination;
    }

    public int trapDamage(Position p){
        return Math.max(
            grid[p.xNext()][p.yNext()].getDamage(),
            grid[p.xPrev()][p.yPrev()].getDamage()
        );
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

    public void setSortie(Point sortie) {
        this.sortie = sortie;
    }

    public Point getSortie() {
        return sortie;
    }

    public boolean arriveSortie(Position p) {
        return (int)p.x == sortie.x && (int)p.y == sortie.y ;
    }
}
