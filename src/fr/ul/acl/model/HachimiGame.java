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
    private final GameLevel level;
    private List<Monstre> monstres;
	private BlockEffects effects;
	private String message = "";
	private String visualEffect = "";
	private boolean stopped = false;

	private final static int DEFAULT_MOVES = 3;
	
	public HachimiGame(String source) throws IOException {
        this(source, GameLevel.getDefaultLevel());
    }

    /** Construit une partie sur le niveau choisi. */
    public HachimiGame(String source, GameLevel level) throws IOException {
        if (level == null) {
            throw new IllegalArgumentException("Le niveau ne peut pas etre null.");
        }
        this.level = level;
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
		if(stopped)
			return;

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
			case INTERACT:

				if (effects.exit()) {
					won = true;
				}

				Point des = effects.destination();

				if (des != null) {
					visualEffect = "lightning";
					stopped = true;
				}

				Trigger t = effects.trigger();
				if (t != null) {
					t.activate();
				}
			
			case IDLE:
				hero.setDirection(0, 0);
				return hero.getPos();
		}

		Position pos = hero.getNewPos();

		if(!plateau.isInsideMap(pos)){
			message = "You cannot leave the map.";
			return hero.getPos();
		}
			

		if (!plateau.isFree(pos)){
			message = "An obstacle is blocking your way.";
			return hero.getPos();
		}

		hero.move();

		effects = plateau.interact(pos);
		if(plateau.isInteracable())
			message = "Press SPACE to interact";
		else
			message = "";

		hero.takeDamage(effects.damage());

		return pos;
	}

	/**
	 * verifier si le jeu est fini
	 */
	@Override
	public boolean isFinished() {
		return won || hero.isDead();
	}

	public GameLevel getLevel() {
        return level;
    }


	public void toggleStopped(){
		this.stopped = !stopped;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Hero getHero() {
		return hero;
	}

	public String getMessage(){
		return message;
	}

	public String getVisualEffect(){
		return visualEffect;
	}

	public void unsetVisual(){
		visualEffect = "";
		message = "";
		stopped = false;
	
		Point des = effects.destination();
		if (des != null) 
			hero.teleport(des.x, des.y);
	}

	public List<Monstre> getMonstres(){
        return monstres;
    } 

	@Override
	public boolean hasWon() {
		return won;
	}
	
	public void recommencer() throws IOException {

	    MapReader lecteur = new MapReader(level.getMapFile());
	    plateau = lecteur.getPlateau();

	    Point depart = lecteur.getDepart();
	    hero = new Hero(depart.x, depart.y);

		monstres = lecteur.getMonstres();
		initializeMonsterDirection();

	    won = false;
		stopped = false;
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

