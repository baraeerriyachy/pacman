
package fr.ul.acl.model;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Plateau {

    private final int largeur;
    private final int hauteur;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private Point sortie;

    public Plateau(int largeur, int hauteur) {
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException(
                "Les dimensions doivent être positives."
            );
        }

        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public boolean estDansLimites(float x, float y) {
        return x >= 0 && x + 1 <= largeur
        && y >= 0 && y + 1 <= hauteur;
    }

    public boolean estObstacle(int x, int y) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getX() == x && obstacle.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public boolean peut_passer(float x, float y) {
    for (Obstacle obstacle : obstacles) {
        if (x < obstacle.getX() + 1
            && x + 1 > obstacle.getX()
            && y < obstacle.getY() + 1
            && y + 1 > obstacle.getY()) {
            return false;
        }
    }
    return true;
}

    public boolean ajouterObstacle(int x, int y) {
        if (!estDansLimites(x, y) || estObstacle(x, y)) {
            return false;
        }

        obstacles.add(new Obstacle(x, y));
        return true;
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

    public boolean arriveSortie(int x, int y) {
        return x == sortie.x && y == sortie.y ;
    }
}
