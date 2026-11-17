
package fr.ul.acl.engine;

public class GameClock {

    private long elapsedTime;
    private long lastUpdate;
    private boolean running;

    public void start() {
        elapsedTime = 0;
        lastUpdate = System.nanoTime();
        running = true;
    }

    public void resume() {
        if (!running) {
            lastUpdate = System.nanoTime();
            running = true;
        }
    }

    public void pause() {
        running = false;
    }

    public void update() {
        if (!running) return;

        long now = System.nanoTime();
        elapsedTime += now - lastUpdate;
        lastUpdate = now;
    }

    public long getTime() {
        return elapsedTime;
    }

    public boolean isRunning() {
        return running;
    }
}
