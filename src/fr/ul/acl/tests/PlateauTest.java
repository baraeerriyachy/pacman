package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Plateau;

/**
 * Tests de Plateau.
 * On utilise un plateau 10 x 8 (largeur differente de la hauteur) pour
 * detecter une confusion entre x/y ou largeur/hauteur.
 * Le heros occupe une case de 1 x 1 dont (x, y) est le coin haut-gauche.
 */
public class PlateauTest {

    private Plateau nouveauPlateau() {
        return new Plateau(10, 8);
    }

    // ---------- construction ----------

    @Test
    public void dimensionsDuPlateau() {
        Plateau p = nouveauPlateau();
        assertEquals(10, p.getLargeur());
        assertEquals(8, p.getHauteur());
    }

    @Test
    public void dimensionsInvalides() {
        assertThrows(IllegalArgumentException.class, () -> new Plateau(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(5, -1));
        assertThrows(IllegalArgumentException.class, () -> new Plateau(0, 0));
    }

    // ---------- estDansLimites ----------

    @Test
    public void coinsDuPlateauSontDansLesLimites() {
        Plateau p = nouveauPlateau();
        assertTrue(p.estDansLimites(0, 0));
        assertTrue(p.estDansLimites(9, 0));
        assertTrue(p.estDansLimites(0, 7));
        assertTrue(p.estDansLimites(9, 7));
    }

    @Test
    public void positionDecimaleALInterieurEstDansLesLimites() {
        Plateau p = nouveauPlateau();
        assertTrue(p.estDansLimites(4.5f, 3.2f));
    }

    @Test
    public void depassementAGaucheEtEnHaut() {
        Plateau p = nouveauPlateau();
        assertFalse(p.estDansLimites(-0.1f, 0));
        assertFalse(p.estDansLimites(0, -0.1f));
    }

    @Test
    public void depassementADroiteEtEnBas() {
        Plateau p = nouveauPlateau();
        // le heros fait 1 de large : x = 9.5 deborde (9.5 + 1 > 10)
        assertFalse(p.estDansLimites(9.5f, 0));
        assertFalse(p.estDansLimites(10, 0));
        assertFalse(p.estDansLimites(0, 7.5f));
        assertFalse(p.estDansLimites(0, 8));
    }

    @Test
    public void largeurEtHauteurNeSontPasInversees() {
        Plateau p = nouveauPlateau();
        assertTrue(p.estDansLimites(9, 0));   // 9 est valide en largeur
        assertFalse(p.estDansLimites(0, 9));  // mais pas en hauteur (8)
    }

    // ---------- ajouterObstacle / estObstacle ----------

    @Test
    public void ajouterUnObstacleValide() {
        Plateau p = nouveauPlateau();
        assertTrue(p.ajouterObstacle(3, 2));
        assertTrue(p.estObstacle(3, 2));
    }

    @Test
    public void obstacleNEstPasTranspose() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 2);
        assertFalse(p.estObstacle(2, 3));
    }

    @Test
    public void caseVideNEstPasUnObstacle() {
        Plateau p = nouveauPlateau();
        assertFalse(p.estObstacle(0, 0));
        assertFalse(p.estObstacle(5, 5));
    }

    @Test
    public void obstacleDansLesQuatreCoins() {
        Plateau p = nouveauPlateau();
        assertTrue(p.ajouterObstacle(0, 0));
        assertTrue(p.ajouterObstacle(9, 0));
        assertTrue(p.ajouterObstacle(0, 7));
        assertTrue(p.ajouterObstacle(9, 7));
    }

    @Test
    public void obstacleHorsDuPlateauEstRefuse() {
        Plateau p = nouveauPlateau();
        assertFalse(p.ajouterObstacle(10, 0));
        assertFalse(p.ajouterObstacle(0, 8));
        assertFalse(p.ajouterObstacle(-1, 0));
        assertFalse(p.ajouterObstacle(0, -1));
        assertFalse(p.estObstacle(10, 0));
    }

    @Test
    public void obstacleEnDoubleEstRefuse() {
        Plateau p = nouveauPlateau();
        assertTrue(p.ajouterObstacle(4, 4));
        assertFalse(p.ajouterObstacle(4, 4));
        assertTrue(p.estObstacle(4, 4));
    }

    // ---------- peut_passer ----------

    @Test
    public void toutPeutPasserSansObstacle() {
        Plateau p = nouveauPlateau();
        assertTrue(p.peut_passer(0, 0));
        assertTrue(p.peut_passer(5.5f, 2.5f));
    }

    @Test
    public void onNePeutPasPasserSurUnObstacle() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        assertFalse(p.peut_passer(3, 3));
    }

    @Test
    public void onPeutPasserSurLesCasesVoisinesDeLObstacle() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        assertTrue(p.peut_passer(2, 3));
        assertTrue(p.peut_passer(4, 3));
        assertTrue(p.peut_passer(3, 2));
        assertTrue(p.peut_passer(3, 4));
    }

    @Test
    public void onPeutPasserEnDiagonaleDeLObstacle() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        assertTrue(p.peut_passer(2, 2));
        assertTrue(p.peut_passer(4, 4));
    }

    @Test
    public void chevauchementPartielEstUnBlocage() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        assertFalse(p.peut_passer(2.5f, 3));
        assertFalse(p.peut_passer(3.5f, 3));
        assertFalse(p.peut_passer(3, 2.5f));
        assertFalse(p.peut_passer(3, 3.5f));
        assertFalse(p.peut_passer(2.5f, 2.5f));
    }

    @Test
    public void toucherLeBordDeLObstacleSansLeChevaucherEstPermis() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        assertTrue(p.peut_passer(2.0f, 3));    // colle a gauche
        assertFalse(p.peut_passer(2.01f, 3));  // commence a entrer
    }

    @Test
    public void passageEntreDeuxObstacles() {
        Plateau p = nouveauPlateau();
        p.ajouterObstacle(3, 3);
        p.ajouterObstacle(5, 3);
        assertTrue(p.peut_passer(4, 3));       // case libre entre les deux
        assertFalse(p.peut_passer(4.1f, 3));   // mord sur l'obstacle de droite
        assertFalse(p.peut_passer(3.9f, 3));   // mord sur l'obstacle de gauche
    }

    // ---------- sortie ----------

    @Test
    public void sortieNonDefinieAuDepart() {
        Plateau p = nouveauPlateau();
        assertNull(p.getSortie());
    }

    @Test
    public void definirEtLireLaSortie() {
        Plateau p = nouveauPlateau();
        Point s = new Point(2, 3);
        p.setSortie(s);
        assertEquals(s, p.getSortie());
    }

    @Test
    public void arriveSortieSeulementSurLaCaseDeSortie() {
        Plateau p = nouveauPlateau();
        p.setSortie(new Point(2, 3));
        assertTrue(p.arriveSortie(2, 3));
        assertFalse(p.arriveSortie(3, 2));   // x et y inverses
        assertFalse(p.arriveSortie(2, 2));
        assertFalse(p.arriveSortie(0, 0));
    }
}
