package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import fr.ul.acl.engine.Cmd;
import fr.ul.acl.model.*;

/** Sprint 3 : test unitaire et comportement au contact d'un piege. */
class TrapTest {
    private final TestMapSupport maps = new TestMapSupport();
    private static final float EPS = 0.0001f;

    @AfterEach void clean() throws IOException { maps.clean(); }

    @Test void piegeContientLaValeurDesDegats() {
        Block trap = Block.trap(10, null);
        assertEquals(Block.Type.TRAP, trap.getType());
        assertEquals(10, trap.getDamage());
    }

    @Test void interactionAvecPiegeRendLesDegats() {
        Plateau p = new Plateau(4, 3);
        p.setBlock(2, 1, Block.trap(10, null));
        assertEquals(10, p.interact(new Position(2, 1)).damage());
        assertEquals(0, p.interact(new Position(0, 0)).damage());
    }

    @Test void contactAvecPiegeDiminuePointsDeVie() throws Exception {
        String file = maps.create("easy", """
                __ __ __ __
                __ HR TR __
                __ EX __ __
                """);
        HachimiGame game = new HachimiGame(new GameLevel("Piege", 1, file));
        assertEquals(100, game.getHero().getHealthPoints());
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        assertEquals(90, game.getHero().getHealthPoints());
        assertEquals(1.4f, game.getHero().getPos().x, EPS);
    }

    @Test void invulnerabiliteEviteDegatsImmediatsRepetes() throws Exception {
        String file = maps.create("easy", """
                __ __ __ __
                __ HR TR __
                __ EX __ __
                """);
        HachimiGame game = new HachimiGame(new GameLevel("Piege", 1, file));
        game.evolve(Cmd.RIGHT);
        game.evolve(Cmd.RIGHT);
        int life = game.getHero().getHealthPoints();
        game.evolve(Cmd.RIGHT);
        assertEquals(life, game.getHero().getHealthPoints());
    }
}
