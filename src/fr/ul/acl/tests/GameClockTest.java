package fr.ul.acl.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import fr.ul.acl.engine.GameClock;

class GameClockTest {
    @Test void demarrerActiveHorlogeEtRemetTempsAZero() {
        GameClock clock = new GameClock();
        clock.start();
        assertTrue(clock.isRunning());
        assertEquals(0L, clock.getTime());
    }

    @Test void pauseBloqueLesMisesAJour() {
        GameClock clock = new GameClock();
        clock.start();
        clock.update();
        clock.pause();
        long time = clock.getTime();
        clock.update();
        assertFalse(clock.isRunning());
        assertEquals(time, clock.getTime());
    }

    @Test void repriseRelanceHorloge() {
        GameClock clock = new GameClock();
        clock.start();
        clock.pause();
        clock.resume();
        assertTrue(clock.isRunning());
        clock.update();
        assertTrue(clock.getTime() >= 0);
    }

    @Test void tempsNeDiminuePasQuandHorlogeTourne() {
        GameClock clock = new GameClock();
        clock.start();
        clock.update();
        long first = clock.getTime();
        clock.update();
        assertTrue(clock.getTime() >= first);
    }
}
