package fr.ul.acl.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Point;


import fr.ul.acl.engine.Cmd;
import fr.ul.acl.engine.Game;

import java.util.*;

public class HachimiGame implements Game {
	private Plateau plateau;
    private Hero hero;
	private boolean won;
	private long startTime;
	private long elapsedSeconds;
    private List<Monstre> monstres;

	private final static int DEFAULT_MOVES = 3;
	
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

		recommencer();
	}

	/**
	 * faire evoluer le jeu suite a une commande
	 * 
	 * @param commande
	 */
	@Override
	public void evolve(Cmd commande) {
		Position pHero = evolveHero(commande);
		for (Monstre m : monstres){
			m.move();
			if (pHero.isOverlapping(m.getPos())) {
				hero.takeDamage(m.getDamage());
			}
		}
	}
	
	public Position evolveHero(Cmd commande){
		switch (commande) {
			case LEFT:
				hero.setDirection(-1, 0);
				break;
			case RIGHT:
				hero.setDirection(1, 0);
				break;
			case UP:
				hero.setDirection(0, -1);
				break;
			case DOWN:
				hero.setDirection(0, 1);
				break; 
			case IDLE:
				return hero.getPos();
		}

		Position pos = hero.getNewPos();

		if (plateau.isFree(pos))
			hero.move();
		else
			return hero.getPos();

		hero.takeDamage(plateau.trapDamage(pos));

		Point des = plateau.portalActive(pos);
		if(des != null)
			hero.teleport(des.x, des.y);
		
		if (plateau.arriveSortie(pos)){
			won = true;
			elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
		}

		return pos;
	}

	/**
	 * verifier si le jeu est fini
	 */
	@Override
	public boolean isFinished() {
		return won || hero.isDead();
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Hero getHero() {
		return hero;
	}

	public List<Monstre> getMonstres(){
        return monstres;
    } 
	
	@Override
	public long getElapsedSeconds() {
		return elapsedSeconds;
	}

	@Override
	public boolean hasWon() {
		return won;
	}
	
	public void recommencer() throws IOException {
	    MapReader lecteur = new MapReader("map.txt");
	    plateau = lecteur.getPlateau();

	    Point depart = lecteur.getDepart();
	    hero = new Hero(depart.x, depart.y);

		monstres = lecteur.getMonstres();
		initializeMonsterDirection();

	    won = false;
	    elapsedSeconds = 0;
	    startTime = System.currentTimeMillis();
	}
	
	private int getMaxMove(Monstre m, Point direction) {
		int max = 0;

		for (int i = 1; i <= DEFAULT_MOVES; i++) {
			Position next = new Position(
				m.getPos().x + direction.x * i,
				m.getPos().y + direction.y * i
			);

			if (!plateau.isInsideMap(next)) {
				break;
			}

			if(!m.isIntangible()){
				if (!plateau.isFree(next)) {
					break;
				}
			}

			max++;
		}

		return max;
	}

	private void initializeMonsterDirection() {
		Point[] directions = {
			new Point(1, 0),
			new Point(-1, 0),
			new Point(0, 1),
			new Point(0, -1)
		};
		
		for (Monstre m : monstres) {

			Point direction = directions[(int) (Math.random() * directions.length)];

			int max = getMaxMove(m, direction);

			if (max == 0) {
				direction = new Point(-direction.x, -direction.y);
				max = getMaxMove(m, direction);
			}

			if (max == 0) {
				// Change axis
				direction = direction.x != 0
					? new Point(0, 1)
					: new Point(1, 0);

				max = getMaxMove(m, direction);

				if (max == 0) {
					direction = new Point(-direction.x, -direction.y);
					max = getMaxMove(m, direction);
				}
			}

			m.setDirection(direction.x, direction.y);
			m.setMaxMove(max);
		}
	}
}

