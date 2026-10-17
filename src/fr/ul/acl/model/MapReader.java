package fr.ul.acl.model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class MapReader {

    private Plateau plateau;
    private Point depart;

    public MapReader(String nomFichier) throws IOException {
        InputStream flux =
            getClass().getResourceAsStream("/" + nomFichier);

        if (flux == null) {
            throw new IOException("Carte introuvable : " + nomFichier);
        }

        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(flux, StandardCharsets.UTF_8))) {

            List<String> lignes = new ArrayList<>();
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {
                lignes.add(ligne);
            }

            if (lignes.isEmpty()) {
                throw new IOException("La carte est vide.");
            }

            int hauteur = lignes.size();
            int largeur = lignes.get(0).length();

            plateau = new Plateau(largeur, hauteur);

            for (int y = 0; y < hauteur; y++) {
                ligne = lignes.get(y);

                if (ligne.length() != largeur) {
                    throw new IOException(
                        "Les lignes de la carte doivent avoir la même longueur."
                    );
                }

                for (int x = 0; x < ligne.length(); x++) {
                    switch (ligne.charAt(x)) {
                        case '0':
                            break;

                        case '1':
                            plateau.ajouterObstacle(x, y);
                            break;
                        
                        case 'S':
                            if (plateau.getSortie() != null) {
                                throw new IOException(
                                    "Plusieurs positions S trouvées."
                                );
                            }
                            plateau.setSortie(new Point(x, y));
                            break;

                        case 'H':
                            if (depart != null) {
                                throw new IOException(
                                    "Plusieurs positions H trouvées."
                                );
                            }
                            depart = new Point(x, y);
                            break;

                        default:
                            throw new IOException(
                                "Symbole invalide : " + ligne.charAt(x)
                            );
                    }
                }
            }

            if (depart == null) {
                throw new IOException("Position H introuvable.");
            }

            if (plateau.getSortie() == null) {
                throw new IOException("Aucune sortie S trouvée.");
            }
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Point getDepart() {
        return depart;
    }
}