package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.Hero;
import fr.ul.acl.model.MapReader;
import fr.ul.acl.model.Plateau;

/**
 * Tests de HachimiGame (la logique du jeu, sans fenetre).
 *
 * Le constructeur charge toujours la carte "map.txt". Pour que ces tests
 * restent valables si la carte change, on ne suppose rien sur son contenu :
 * on cherche sur le plateau les cases utiles (case libre, obstacle, sortie)
 * et on place le heros avec hero.deplacer(x, y).
 */
public class HachimiGameTest {

    private static final float DELTA = 0.001f;
    private static final String AIDE_ABSENTE = "fichier_aide_qui_n_existe_pas.txt";

    private HachimiGame nouvellePartie() throws Exception {
        return new HachimiGame(AIDE_ABSENTE);
    }

    /**
     * Cherche une case entiere (x, y) ou le heros peut se trouver
     * ET faire un pas (dx, dy) sans sortir ni toucher un obstacle.
     */
    private static int[] chercherCaseOuLePasEstPossible(Plateau p, float dx, float dy) {
        for (int y = 0; y < p.getHauteur(); y++) {
            for (int x = 0; x < p.getLargeur(); x++) {
                if (p.estDansLimites(x, y) && p.peut_passer(x, y)
                        && p.estDansLimites(x + dx, y + dy)
                        && p.peut_passer(x + dx, y + dy)) {
                    return new int[] { x, y };
                }
            }
        }
        return null;
    }

    private void verifierPas(Cmd cmd, int sx, int sy) throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        float dx = sx * h.getVitesse();
        float dy = sy * h.getVitesse();
        int[] pos = chercherCaseOuLePasEstPossible(jeu.getPlateau(), dx, dy);
        assertNotNull(pos, "Aucune case de la carte ne permet ce deplacement");

        h.deplacer(pos[0], pos[1]);
        jeu.evolve(cmd);

        assertEquals(pos[0] + dx, h.getX(), DELTA);
        assertEquals(pos[1] + dy, h.getY(), DELTA);
    }

    // ---------- demarrage ----------

    @Test
    public void laPartieDemarreMemeSansFichierDAide() throws Exception {
        HachimiGame jeu = nouvellePartie();
        assertNotNull(jeu.getPlateau());
        assertNotNull(jeu.getHero());
    }

    @Test
    public void etatInitial() throws Exception {
        HachimiGame jeu = nouvellePartie();
        assertFalse(jeu.isFinished());
        assertEquals(0L, jeu.getElapsedSeconds());
    }

    @Test
    public void leHerosDemarreSurLaCaseDeDepartDeLaCarte() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Point depart = new MapReader("map.txt").getDepart();
        assertEquals((float) depart.x, jeu.getHero().getX(), DELTA);
        assertEquals((float) depart.y, jeu.getHero().getY(), DELTA);
    }

    @Test
    public void leHerosDemarreSurUneCaseLibre() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        assertTrue(jeu.getPlateau().estDansLimites(h.getX(), h.getY()));
        assertTrue(jeu.getPlateau().peut_passer(h.getX(), h.getY()));
    }

    // ---------- commandes ----------

    @Test
    public void commandeIdleNeBougePasLeHeros() throws Exception {
        HachimiGame jeu = nouvellePartie();
        float x = jeu.getHero().getX();
        float y = jeu.getHero().getY();
        jeu.evolve(Cmd.IDLE);
        assertEquals(x, jeu.getHero().getX(), DELTA);
        assertEquals(y, jeu.getHero().getY(), DELTA);
        assertFalse(jeu.isFinished());
    }

    @Test
    public void deplacementVersLaDroite() throws Exception {
        verifierPas(Cmd.RIGHT, 1, 0);
    }

    @Test
    public void deplacementVersLaGauche() throws Exception {
        verifierPas(Cmd.LEFT, -1, 0);
    }

    @Test
    public void deplacementVersLeBas() throws Exception {
        verifierPas(Cmd.DOWN, 0, 1);
    }

    @Test
    public void deplacementVersLeHaut() throws Exception {
        verifierPas(Cmd.UP, 0, -1);
    }

    @Test
    public void plusieursPasConsecutifsVersLaDroite() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        float pas = h.getVitesse();
        int[] pos = chercherCaseOuLePasEstPossible(jeu.getPlateau(), 2 * pas, 0);
        assertNotNull(pos);
        h.deplacer(pos[0], pos[1]);
        jeu.evolve(Cmd.RIGHT);
        jeu.evolve(Cmd.RIGHT);
        assertEquals(pos[0] + 2 * pas, h.getX(), DELTA);
        assertEquals(pos[1], h.getY(), DELTA);
    }

    // ---------- limites du plateau ----------

    @Test
    public void leHerosNeSortPasParLaGauche() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        h.deplacer(0, 0);
        jeu.evolve(Cmd.LEFT);
        assertEquals(0f, h.getX(), DELTA);
    }

    @Test
    public void leHerosNeSortPasParLeHaut() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        h.deplacer(0, 0);
        jeu.evolve(Cmd.UP);
        assertEquals(0f, h.getY(), DELTA);
    }

    @Test
    public void leHerosNeSortPasParLaDroite() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        float xMax = jeu.getPlateau().getLargeur() - 1;
        h.deplacer(xMax, 0);
        jeu.evolve(Cmd.RIGHT);
        assertEquals(xMax, h.getX(), DELTA);
    }

    @Test
    public void leHerosNeSortPasParLeBas() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Hero h = jeu.getHero();
        float yMax = jeu.getPlateau().getHauteur() - 1;
        h.deplacer(0, yMax);
        jeu.evolve(Cmd.DOWN);
        assertEquals(yMax, h.getY(), DELTA);
    }

    // ---------- obstacles ----------

    @Test
    public void unObstacleBloqueLeHerosVenantDeLaGauche() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Plateau p = jeu.getPlateau();
        int ox = -1;
        int oy = -1;
        for (int y = 0; y < p.getHauteur() && ox < 0; y++) {
            for (int x = 1; x < p.getLargeur(); x++) {
                if (p.estObstacle(x, y)) {
                    ox = x;
                    oy = y;
                    break;
                }
            }
        }
        assertTrue(ox >= 1, "La carte doit contenir au moins un obstacle (colonne >= 1)");

        Hero h = jeu.getHero();
        h.deplacer(ox - 1, oy);
        jeu.evolve(Cmd.RIGHT);
        assertEquals((float) (ox - 1), h.getX(), DELTA);
        assertEquals((float) oy, h.getY(), DELTA);
    }

    @Test
    public void unObstacleBloqueLeHerosVenantDuHaut() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Plateau p = jeu.getPlateau();
        int ox = -1;
        int oy = -1;
        for (int y = 1; y < p.getHauteur() && ox < 0; y++) {
            for (int x = 0; x < p.getLargeur(); x++) {
                if (p.estObstacle(x, y)) {
                    ox = x;
                    oy = y;
                    break;
                }
            }
        }
        assertTrue(ox >= 0, "La carte doit contenir au moins un obstacle (ligne >= 1)");

        Hero h = jeu.getHero();
        h.deplacer(ox, oy - 1);
        jeu.evolve(Cmd.DOWN);
        assertEquals((float) ox, h.getX(), DELTA);
        assertEquals((float) (oy - 1), h.getY(), DELTA);
    }

    // ---------- victoire ----------

    @Test
    public void pasDeVictoireAuDepart() throws Exception {
        HachimiGame jeu = nouvellePartie();
        jeu.evolve(Cmd.IDLE);
        assertFalse(jeu.isFinished());
    }

    @Test
    public void atteindreLaSortieTermineLeJeu() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Point sortie = jeu.getPlateau().getSortie();
        jeu.getHero().deplacer(sortie.x, sortie.y);
        jeu.evolve(Cmd.IDLE);
        assertTrue(jeu.isFinished());
    }

    @Test
    public void etreSurLaCaseVoisineDeLaSortieNeTerminePasLeJeu() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Point sortie = jeu.getPlateau().getSortie();
        int x = (sortie.x > 0) ? sortie.x - 1 : sortie.x + 1;
        jeu.getHero().deplacer(x, sortie.y);
        jeu.evolve(Cmd.IDLE);
        assertFalse(jeu.isFinished());
    }

    @Test
    public void leTempsDeJeuEstCalculeALaVictoire() throws Exception {
        HachimiGame jeu = nouvellePartie();
        Thread.sleep(1100);
        Point sortie = jeu.getPlateau().getSortie();
        jeu.getHero().deplacer(sortie.x, sortie.y);
        jeu.evolve(Cmd.IDLE);
        assertTrue(jeu.isFinished());
        assertTrue(jeu.getElapsedSeconds() >= 1,
                "Le temps ecoule doit etre d'au moins 1 seconde");
    }
}