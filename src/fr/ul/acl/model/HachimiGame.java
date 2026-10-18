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
	private static final float pas = 0.2f;
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
		
		float dx = 0;
        float dy = 0;

		switch (commande) {
			case LEFT:
				dx = -pas;
				break;
			case RIGHT:
				dx = pas;
				break;
			case UP:
				dy = -pas;
				break;
			case DOWN:
				dy = pas;
				break;
		}

		float nx = hero.getX() + dx; nx = Math.round(nx * 100f) / 100f;
		float ny = hero.getY() + dy; ny = Math.round(ny * 100f) / 100f;

		if (!plateau.estDansLimites(nx, ny)) {
			System.out.println(
				"Déplacement impossible : hors limites."
			);
		} else if (!plateau.peut_passer(nx, ny)) {
			System.out.println(
				"Déplacement impossible : obstacle."
			);
		} else {
			hero.deplacer(nx, ny);
		}

		if (plateau.arriveSortie((int)hero.getX(), (int)hero.getY())){
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

