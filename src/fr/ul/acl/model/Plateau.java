
package fr.ul.acl.model;

import java.util.ArrayList;
import java.util.List;

public class Plateau {

    private final int largeur;
    private final int hauteur;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public Plateau(int largeur, int hauteur) {
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException(
                "Les dimensions doivent être positives."
            );
        }

        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public boolean estDansLimites(int x, int y) {
        return x >= 0 && x < largeur
                && y >= 0 && y < hauteur;
    }

    public boolean estObstacle(int x, int y) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.estEnPosition(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean ajouterObstacle(int x, int y) {
        if (!estDansLimites(x, y) || estObstacle(x, y)) {
            return false;
        }

        obstacles.add(new Obstacle(x, y));
        return true;
    }

    public boolean peutAller(int x, int y) {
        return estDansLimites(x, y) && !estObstacle(x, y);
    }

    public void afficher(Hero hero) {
        for (int y = 0; y < hauteur; y++) {
            StringBuilder ligne = new StringBuilder();

            for (int x = 0; x < largeur; x++) {
                char symbole = '.';

                if (hero.getX() == x && hero.getY() == y) {
                    symbole = 'H';
                } else if (estObstacle(x, y)) {
                    symbole = '#';
                }

                ligne.append(symbole).append(' ');
            }

            System.out.println(ligne);
        }
    }
}
