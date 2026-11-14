package fr.ul.acl.map_tool;

import fr.ul.acl.model.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapReader {

    private final List<Monstre> monstres = new ArrayList<>();
    private final Properties symbols = new Properties();

    private Plateau plateau;
    private Point depart;

    private static final Pattern SYMBOL_PATTERN =
        Pattern.compile("([A-Z]+)(\\d*)");
    
    private void processModele(
        String model,
        String identifiant,
        int x,
        int y,
        String difficulte,
        Map<String, Point> portalEntrances,
        Map<String, Point> portalDestinations
    ) throws IOException {

        switch (model) {

            case "wall":
                plateau.setBlock(x, y, Block.wall());
                break;

            case "zombie":
                monstres.add(Monstre.zombie(
                    x, y, 
                    Config.DAMAGE.get("zombie").get(difficulte), 
                    Config.SPEED.get("zombie").get(difficulte)
                    ));
                break;

            case "ghost":
                monstres.add(Monstre.ghost(
                    x, y, 
                    Config.DAMAGE.get("ghost").get(difficulte), 
                    Config.SPEED.get("ghost").get(difficulte)
                    ));
                break;

            case "trap":
                plateau.setBlock(x, y, Block.trap(
                    Config.DAMAGE.get("trap").get(difficulte)
                ));
                break;

            case "treasure":
                plateau.setBlock(x, y, Block.trigger(new Tresor()));
                break;

            case "lever":
                plateau.setBlock(x, y, Block.trigger(new Lever()));
                break;

            case "portal":
                requireIdentifier(identifiant);
                portalEntrances.put(identifiant, new Point(x, y));
                break;

            case "destination":
                requireIdentifier(identifiant);
                portalDestinations.put(identifiant, new Point(x, y));
                break;

            case "exit":
                plateau.setBlock(x, y, Block.exit());
                break;

            case "hero":
                if (depart != null) {
                    throw new IOException(
                        "Plusieurs positions de départ trouvées."
                    );
                }
                depart = new Point(x, y);
                break;

            default:
                throw new IOException(
                    "Modèle inconnu pour : " + model
                );
        }
    }

    public MapReader(String nomFichier) throws IOException {

        // Load symbol definitions
        try (InputStream flux = getClass().getResourceAsStream(
                "/map/symbols.properties")) {

            if (flux == null) {
                throw new IOException("Fichier symbols.properties introuvable.");
            }

            symbols.load(new InputStreamReader(flux, StandardCharsets.UTF_8));
        }

        // Load map
        InputStream flux = getClass().getResourceAsStream(
            "/map/" + nomFichier
        );

        if (flux == null) {
            throw new IOException("Carte introuvable : " + nomFichier);
        }

        String difficulte = nomFichier.split("/")[0];

        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(flux, StandardCharsets.UTF_8))) {

            List<String[]> lignes = new ArrayList<>();
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.isBlank()) {
                    continue;
                }

                lignes.add(ligne.trim().split("\\s+"));
            }

            if (lignes.isEmpty()) {
                throw new IOException("La carte est vide.");
            }

            int hauteur = lignes.size();
            int largeur = lignes.get(0).length;

            plateau = new Plateau(largeur, hauteur);

            Map<String, Point> portalEntrances = new HashMap<>();
            Map<String, Point> portalDestinations = new HashMap<>();

            for (int y = 0; y < hauteur; y++) {

                String[] cases = lignes.get(y);

                if (cases.length != largeur) {
                    throw new IOException(
                        "Les lignes de la carte doivent avoir le même nombre de cases."
                    );
                }

                for (int x = 0; x < largeur; x++) {

                    String symbole = cases[x];

                    if (symbole.equals("__")) {
                        continue;
                    }

                    Matcher matcher = SYMBOL_PATTERN.matcher(symbole);

                    if (!matcher.matches()) {
                        throw new IOException("Symbole invalide : " + symbole);
                    }

                    String acronyme = matcher.group(1);
                    String identifiant = matcher.group(2);

                    String model = symbols.getProperty(acronyme);

                    if (model == null) {
                        throw new IOException(
                            "Aucun modèle défini pour : " + acronyme
                        );
                    }

                    processModele(
                        model,
                        identifiant,
                        x,
                        y,
                        difficulte,
                        portalEntrances,
                        portalDestinations
                    );
                }
            }

            if (depart == null) {
                throw new IOException("Position de départ introuvable.");
            }

            // Connect portals to their corresponding destinations
            for (Map.Entry<String, Point> entry : portalEntrances.entrySet()) {

                String id = entry.getKey();
                Point destination = portalDestinations.get(id);

                if (destination == null) {
                    throw new IOException(
                        "Destination introuvable pour le portail PT" + id
                    );
                }

                Point entrance = entry.getValue();

                plateau.setBlock(
                    entrance.x,
                    entrance.y,
                    Block.portal(destination)
                );
            }

            // Check for destinations without entrances
            for (String id : portalDestinations.keySet()) {
                if (!portalEntrances.containsKey(id)) {
                    throw new IOException(
                        "Aucun portail correspondant à la destination DT" + id
                    );
                }
            }
        }
    }

    private void requireIdentifier(String identifiant)
            throws IOException {
        if (identifiant.isEmpty()) {
            throw new IOException(
                "Identifiant manquant !"
            );
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Point getDepart() {
        return depart;
    }

    public List<Monstre> getMonstres() {
        return monstres;
    }
}
