package fr.ul.acl.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Point;


import fr.ul.acl.engine.Cmd;
import fr.ul.acl.engine.Game;

import fr.ul.acl.model.MapReader;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 *
 *         Version avec personnage qui peut se deplacer. A completer dans les
 *         versions suivantes.
 * 
 */
public class HachimiGame implements Game {
	private Plateau plateau;
    private Hero hero;
	private boolean won = false;
	private long startTime;
	private long elapsedSeconds;
	/**
	 * constructeur avec fichier source pour le help
	 * 
	 */
	public HachimiGame(String source) throws IOException {
		BufferedReader helpReader;
		try {
			helpReader = new BufferedReader(new FileReader(source));
			String ligne;
			while ((ligne = helpReader.readLine()) != null) {
				System.out.println(ligne);
			}
			helpReader.close();
		} catch (IOException e) {
			System.out.println("Help not available");
		}

		
		MapReader lecteur = new MapReader("map.txt");
		plateau = lecteur.getPlateau();

		Point depart = lecteur.getDepart();
		hero = new Hero(depart.x, depart.y);

		startTime = System.currentTimeMillis();
	}

	/**
	 * faire evoluer le jeu suite a une commande
	 * 
	 * @param commande
	 */
	@Override
	public void evolve(Cmd commande) {
		//System.out.println("Execute "+commande);
		
		int dx = 0;
        int dy = 0;

		switch (commande) {
			case LEFT:
				dx = -1;
				break;
			case RIGHT:
				dx = 1;
				break;
			case UP:
				dy = -1;
				break;
			case DOWN:
				dy = 1;
				break;
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

		if (plateau.arriveSortie(hero.getX(), hero.getY())){
			won = true;
			elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
		}
	}

	

	/**
	 * verifier si le jeu est fini
	 */
	@Override
	public boolean isFinished() {
		return won;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Hero getHero() {
		return hero;
	}
	
	@Override
	public long getElapsedSeconds() {
		return elapsedSeconds;
	}
	// public boolean hasWon() {
	// 	return won;
	// }
}

