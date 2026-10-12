
package fr.ul.acl.start;

import java.util.Scanner;

import fr.ul.acl.model.Hero;
import fr.ul.acl.model.Plateau;

public class Main {

    public static void main(String[] args) {
        Plateau plateau = new Plateau(8, 5);
        Hero hero = new Hero(2, 2);

        // Ajouter un obstacle
        plateau.ajouterObstacle(4, 2);
        plateau.ajouterObstacle(4, 3);
        plateau.ajouterObstacle(4, 4);

        System.out.println("=== Demo Sprint 1 ===");
        System.out.println("H : héros \n# : obstacle \n. : vide");
        System.out.println("=====================");
        plateau.afficher(hero);

        try (Scanner sc = new Scanner(System.in)) {
            boolean continuer = true;

            while (continuer) {
                System.out.print("Commande (L/R/U/D/S) : ");

                if (!sc.hasNextLine()) {
                    break;
                }

                String cmd = sc.nextLine().trim().toUpperCase();

                int dx = 0;
                int dy = 0;

                switch (cmd) {
                    case "L":
                        dx = -1;
                        break;
                    case "R":
                        dx = 1;
                        break;
                    case "U":
                        dy = -1;
                        break;
                    case "D":
                        dy = 1;
                        break;
                    case "S":
                        continuer = false;
                        continue;
                    default:
                        System.out.println("Commande inconnue.");
                        continue;
                }

                int nx = hero.getX() + dx;
                int ny = hero.getY() + dy;

                if (!plateau.estDansLimites(nx, ny)) {
                    System.out.println(
                        "Déplacement impossible : hors limites."
                    );
                } else if (plateau.estObstacle(nx, ny)) {
                    System.out.println(
                        "Déplacement impossible : obstacle."
                    );
                } else {
                    hero.deplacer(dx, dy);
                }

                System.out.println("Position : " + hero);
                plateau.afficher(hero);
            }
        }

        System.out.println("Fin du jeu.");
    }
}
