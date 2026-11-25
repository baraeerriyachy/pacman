package fr.ul.acl.tests;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.Block;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.Plateau;
import fr.ul.acl.model.Position;
import fr.ul.acl.utility.MapReader;

/**
 * Outils de tests : retrouver les positions sur le plateau 
 */
final class TestPositionFinder {
    private static final float EPS = 0.0001f;
    private static final int MAX_PAS_PAR_CASE = 30;

    private TestPositionFinder() {}

    static Point depart(MapReader lecteur) {
        Point p = lecteur.getDepart();
        if (p == null) throw new AssertionError("Aucun depart trouve dans la carte");
        return new Point(p);
    }

    static Point trouverBloc(Plateau plateau, Block.Type type) {
        for (int y = 0; y < plateau.getHauteur(); y++) {
            for (int x = 0; x < plateau.getLargeur(); x++) {
                if (plateau.getBlockType(x, y) == type) return new Point(x, y);
            }
        }
        throw new AssertionError("Aucune case de type " + type + " dans la carte");
    }

    static Point trouverCaseLibre(Plateau plateau, Point differenteDe) {
        for (int y = 0; y < plateau.getHauteur(); y++) {
            for (int x = 0; x < plateau.getLargeur(); x++) {
                Point candidate = new Point(x, y);
                if ((differenteDe == null || !differenteDe.equals(candidate))
                        && plateau.getBlockType(x, y) == Block.Type.EMPTY
                        && plateau.isFree(new Position(x, y))) {
                    return candidate;
                }
            }
        }
        throw new AssertionError("Aucune case libre trouvee");
    }

    enum Bord { GAUCHE, HAUT }

    static Point trouverCaseLibreSurBord(Plateau plateau, Bord bord) {
        for (int y = 0; y < plateau.getHauteur(); y++) {
            for (int x = 0; x < plateau.getLargeur(); x++) {
                if ((bord == Bord.GAUCHE && x != 0) || (bord == Bord.HAUT && y != 0)) continue;
                if (plateau.getBlockType(x, y) == Block.Type.EMPTY
                        && plateau.isFree(new Position(x, y))) return new Point(x, y);
            }
        }
        throw new AssertionError("Aucune case libre sur le bord " + bord);
    }

    record ApprocheMur(Point caseAvantMur, Cmd directionVersMur) {}

    static ApprocheMur trouverApprocheMur(Plateau plateau) {
        for (int y = 0; y < plateau.getHauteur(); y++) {
            for (int x = 0; x < plateau.getLargeur(); x++) {
                if (plateau.getBlockType(x, y) != Block.Type.WALL) continue;
                Point avant = new Point(x - 1, y);
                if (estLibre(plateau, avant)) return new ApprocheMur(avant, Cmd.RIGHT);
                avant = new Point(x + 1, y);
                if (estLibre(plateau, avant)) return new ApprocheMur(avant, Cmd.LEFT);
                avant = new Point(x, y - 1);
                if (estLibre(plateau, avant)) return new ApprocheMur(avant, Cmd.DOWN);
                avant = new Point(x, y + 1);
                if (estLibre(plateau, avant)) return new ApprocheMur(avant, Cmd.UP);
            }
        }
        throw new AssertionError("Aucun mur avec case d'approche libre");
    }

    private static boolean estLibre(Plateau p, Point c) {
        return c.x >= 0 && c.y >= 0
                && c.x < p.getLargeur() && c.y < p.getHauteur()
                && p.getBlockType(c.x, c.y) == Block.Type.EMPTY
                && p.isFree(new Position(c.x, c.y));
    }

    /**
     * Recherche un chemin sur le plateau par parcours en largeur.
     * Ensuite deplace le heros uniquement par les commandes normales du jeu.
     * Evite d'utiliser hero.teleport() pour preparer le test.
     */
    static void rejoindre(HachimiGame jeu, Point destination) {
        Plateau plateau = jeu.getPlateau();
        Position position = jeu.getHero().getPos();
        Point debut = new Point(Math.round(position.x), Math.round(position.y));
        if (Math.abs(position.x - debut.x) > EPS || Math.abs(position.y - debut.y) > EPS) {
            throw new AssertionError("Le heros doit etre au centre d'une case avant de rechercher un chemin");
        }
        if (!plateau.isInsideMap(new Position(destination.x, destination.y))
                || plateau.getBlockType(destination.x, destination.y) == Block.Type.WALL) {
            throw new AssertionError("Destination inaccessible : " + destination);
        }
        Queue<Point> attente = new ArrayDeque<>();
        Map<Point, Point> precedent = new HashMap<>();
        attente.add(debut);
        precedent.put(debut, null);
        final int[][] deltas = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        while (!attente.isEmpty() && !precedent.containsKey(destination)) {
            Point p = attente.remove();
            for (int[] delta : deltas) {
                Point suivant = new Point(p.x + delta[0], p.y + delta[1]);
                if (!precedent.containsKey(suivant) &&
                        suivant.x >= 0 && suivant.y >= 0 &&
                        suivant.x < plateau.getLargeur() && suivant.y < plateau.getHauteur() &&
                        plateau.getBlockType(suivant.x, suivant.y) != Block.Type.WALL) {
                    precedent.put(suivant, p);
                    attente.add(suivant);
                }
            }
        }
        if (!precedent.containsKey(destination)) {
            throw new AssertionError("Aucun chemin trouve vers " + destination);
        }
        List<Point> chemin = new ArrayList<>();
        for (Point p = destination; p != null; p = precedent.get(p)) chemin.add(p);
        Collections.reverse(chemin);

        for (int i = 1; i < chemin.size(); i++) {
            Point cible = chemin.get(i);
            Point avant = chemin.get(i - 1);
            Cmd commande = cible.x > avant.x ? Cmd.RIGHT :
                    cible.x < avant.x ? Cmd.LEFT :
                    cible.y > avant.y ? Cmd.DOWN : Cmd.UP;
            boolean arrive = false;
            for (int pas = 0; pas < MAX_PAS_PAR_CASE; pas++) {
                Position pos = jeu.getHero().getPos();
                if (Math.abs(pos.x - cible.x) <= EPS && Math.abs(pos.y - cible.y) <= EPS) {
                    arrive = true;
                    break;
                }
                float xAvant = pos.x, yAvant = pos.y;
                jeu.evolve(commande);
                Position apres = jeu.getHero().getPos();
                if (Math.abs(apres.x - xAvant) <= EPS && Math.abs(apres.y - yAvant) <= EPS) {
                    throw new AssertionError("Deplacement bloque pendant la recherche de " + destination);
                }
            }
            if (!arrive && (Math.abs(jeu.getHero().getPos().x - cible.x) > EPS
                    || Math.abs(jeu.getHero().getPos().y - cible.y) > EPS)) {
                throw new AssertionError("Impossible d'atteindre la case " + cible);
            }
        }
    }
}
