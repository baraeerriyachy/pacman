package fr.ul.acl.utility;

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
    private record IdentifiedElement(String model, Point position) {}

    private final Map<String, List<IdentifiedElement>> elementsById =
        new HashMap<>();

    private Plateau plateau;
    private Point depart;

    private static final Pattern SYMBOL_PATTERN =
        Pattern.compile("([A-Z]+)(\\d*)");
    
    private void processModele(
        String model,
        String identifiant,
        int x,
        int y,
        String difficulte
    ) throws IOException {

        switch (model) {

            case "wall":
                plateau.setBlock(x, y, Block.wall(null));
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
                    Config.DAMAGE.get("trap").get(difficulte),
                    null
                ));
                break;
            case "bear trap":
                plateau.setBlock(x, y, Block.trap(
                    Config.DAMAGE.get("trap").get(difficulte),
                    Trigger.bear_trap()
                ));
                break;

            case "treasure":
                plateau.setBlock(x, y, Block.trigger(new Tresor()));
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

            case "lever":
            case "portal":
            case "destination":
            case "secret wall":
                registerElement(model, identifiant, x, y);
                break;

            default:
                throw new IOException(
                    "Modèle inconnu pour : " + model
                );
        }
    }

    private void resolveIdentifiedElements() throws IOException {
        for (List<IdentifiedElement> group : elementsById.values()) {
            List<Trigger> triggers = new ArrayList<>();
            List<Mechanism> mechanisms = new ArrayList<>();

            Point entrance = null;
            Point destination = null;

            for (IdentifiedElement element : group) {
                Point position = element.position();
                int x = position.x;
                int y = position.y;

                switch (element.model()) {

                    case "portal" ->
                        entrance = position;

                    case "destination" ->
                        destination = position;

                    case "lever" -> {
                        Trigger lever = Trigger.lever();
                        triggers.add(lever);
                        plateau.setBlock(x, y, Block.trigger(lever));
                    }

                    case "secret wall" -> {
                        Mechanism mechanism = Mechanism.secret_wall();
                        mechanisms.add(mechanism);
                        plateau.setBlock(x, y, Block.wall(mechanism));
                    }
                }
            }

            // Establish mechanism requirements
            for (Mechanism mechanism : mechanisms) {
                mechanism.setRequirements(triggers);
            }

            // Connect portal to destination
            if (entrance != null && destination != null) {
                plateau.setBlock(
                    entrance.x,
                    entrance.y,
                    Block.portal(destination)
                );
            }
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
                        difficulte
                    );
                }
            }

            if (depart == null) {
                throw new IOException("Position de départ introuvable.");
            }

            resolveIdentifiedElements();

            for (Monstre m : monstres) {
                MonsterInitialiser.initialize(m, plateau);
            }
        }
    }

    private void registerElement(String model, String id, int x, int y) throws IOException {
        if (id.isEmpty()) {
            throw new IOException(
                "Identifiant manquant !"
            );
        }

        elementsById.computeIfAbsent(id, k -> new ArrayList<>())
            .add(new IdentifiedElement(model, new Point(x, y)));
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
