package fr.ul.acl.model;

import java.io.IOException;
import java.awt.Point;

import fr.ul.acl.engine.Cmd;
import fr.ul.acl.engine.Game;
import fr.ul.acl.engine.GameClock;

import java.util.*;

import fr.ul.acl.utility.MapReader;

public class HachimiGame implements Game {
	private Plateau plateau;
    private Hero hero;
	private boolean won;
    private final GameLevel level;
    private List<Monstre> monstres;
	private List<TimedGroup> timedGroups;
	
	private BlockEffects effects;
	private String message = "";
	private String visualEffect = "";
	private boolean stopped = false;
	private final GameClock clock = new GameClock();

    /** Construit une partie sur le niveau choisi. */
    public HachimiGame(GameLevel level) throws IOException {
        if (level == null) {
            throw new IllegalArgumentException("Le niveau ne peut pas etre null.");
        }
        this.level = level;
		
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

		clock.update();
		for (TimedGroup group : timedGroups) {
			group.update(clock.getTime());
		}

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
					clock.pause();
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
		if(stopped)
			clock.pause();
		else
			clock.resume();
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
		timedGroups = lecteur.gettimedGroups();

	    won = false;
		stopped = false;
		effects = new BlockEffects(0, null, false, null);
		clock.start();
	}

	public long getElapsedTime() {
		return clock.getTime() / 1_000_000_000L;
	}
}



