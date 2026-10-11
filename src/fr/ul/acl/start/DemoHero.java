package fr.ul.acl.start;

import java.util.Scanner;

import fr.ul.acl.model.Hero;

public class DemoHero {

    private static final int LARGEUR = 8;
    private static final int HAUTEUR = 5;

    public static void main(String[] args) {
        Hero hero = new Hero(2, 2);
        System.out.println("Position initiale : " + hero);
        afficher(hero);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Commande (L/R/U/D/S) : ");
            if (!sc.hasNextLine()) {
                break;
            }
            String cmd = sc.nextLine().trim().toUpperCase();
            if (cmd.equals("S")) {
                break;
            }
            switch (cmd) {
                case "L": hero.deplacer(-1, 0); break;
                case "R": hero.deplacer(1, 0); break;
                case "U": hero.deplacer(0, -1); break;
                case "D": hero.deplacer(0, 1); break;
                default:
                    System.out.println("Commande inconnue");
                    continue;
            }
            System.out.println("Après " + cmd + " : " + hero);
            afficher(hero);
        }
        System.out.println("Fin.");
    }

    private static void afficher(Hero hero) {
        boolean dedans = hero.getX() >= 0 && hero.getX() < LARGEUR
                && hero.getY() >= 0 && hero.getY() < HAUTEUR;
        if (!dedans) {
            System.out.println("(le héros est hors du plateau : les limites ne sont pas encore gérées)");
            return;
        }
        for (int y = 0; y < HAUTEUR; y++) {
            StringBuilder ligne = new StringBuilder();
            for (int x = 0; x < LARGEUR; x++) {
                ligne.append(x == hero.getX() && y == hero.getY() ? 'H' : '.').append(' ');
            }
            System.out.println(ligne);
        }
    }
}