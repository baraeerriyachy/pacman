
package fr.ul.acl.engine;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;

import fr.ul.acl.model.*;

public class GameEngineGraphical {

    private final String helpFile;
    private final HachimiController controller;

    private GraphicalInterface gui;

    private HachimiGame game;
    private GameLevel currentLevel;

    private volatile boolean gameRunning;
    private Thread gameThread;

    private final List<GameLevel> levels =
            GameLevel.getAvailableLevels();

    public GameEngineGraphical(
            String helpFile,
            HachimiController controller) {

        this.helpFile = helpFile;
        this.controller = controller;
    }

    public void run() {
        gui = new GraphicalInterface(controller);
        showLevelSelection();
    }

    private void showLevelSelection() {
        stopGameLoop(() -> {
            gui.showLevelSelection(levels, this::startLevel);
        });
    }

    private void startLevel(GameLevel level) {
        stopGameLoop(() -> {
            try {
                currentLevel = level;

                game = new HachimiGame(helpFile, currentLevel);

                HachimiPainter painter = new HachimiPainter(game);

                controller.resetCommand();

                gui.showGame(
                    painter,
                    controller,
                    game::toggleStopped,
                    this::retryLevel,
                    this::backToLevelSelection,
                    game.getLevel().getName()
                );

                gui.paint();

                startGameThread();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void startGameThread() {
        gameRunning = true;

        HachimiGame currentGame = game;

        gameThread = new Thread(() -> {
            try {
                while (gameRunning && !currentGame.isFinished()) {

                    Cmd command = controller.getCommand();

                    if (!gameRunning) {
                        break;
                    }

                    currentGame.evolve(command);

                    SwingUtilities.invokeLater(() -> {
                        if (game == currentGame && gameRunning) {
                            gui.paint();
                        }
                    });

                    Thread.sleep(100);
                }

                if (gameRunning && currentGame.isFinished()) {
                    SwingUtilities.invokeLater(
                        () -> finishGame(currentGame)
                    );
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        gameThread.start();
    }

    private void finishGame(HachimiGame finishedGame) {
        if (game != finishedGame || !gameRunning) {
            return;
        }

        gameRunning = false;

        boolean won = finishedGame.hasWon();

        if (won) {
            unlockNextLevel();
        }

        boolean hasNext = getNextLevel() != null;

        GameOverScreen screen = new GameOverScreen(
            won,
            hasNext,
            this::retryLevel,
            this::startNextLevel,
            this::showLevelSelection,
            this::quit
        );

        gui.showGameOver(screen);
    }

    private void unlockNextLevel() {
        int index = levels.indexOf(currentLevel);

        if (index >= 0 && index + 1 < levels.size()) {
            levels.get(index + 1).unlock();
        }
    }

    private GameLevel getNextLevel() {
        int index = levels.indexOf(currentLevel);

        if (index < 0 || index + 1 >= levels.size()) {
            return null;
        }

        GameLevel next = levels.get(index + 1);

        return next.isLocked() ? null : next;
    }

    private void startNextLevel() {
        GameLevel next = getNextLevel();

        if (next != null) {
            startLevel(next);
        }
    }

    private void retryLevel() {
        if (currentLevel != null) {
            startLevel(currentLevel);
        }
    }

    private void backToLevelSelection() {
        int choice = JOptionPane.showConfirmDialog(
            null,
            "Give up the current game and return to level selection?",
            "Leave current game?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            showLevelSelection();
        }
    }

    private void showGameOver(boolean won) {
        GameOverScreen screen = new GameOverScreen(
            won,
            getNextLevel() != null,
            this::retryLevel,
            this::startNextLevel,
            this::showLevelSelection,
            this::quit
        );

        gui.showGameOver(screen);
    }

    private void stopGameLoop(Runnable callback) {
        gameRunning = false;

        Thread previousThread = gameThread;

        if (previousThread == null || !previousThread.isAlive()) {
            callback.run();
            return;
        }

        Thread stopper = new Thread(() -> {
            try {
                previousThread.join();
                SwingUtilities.invokeLater(callback);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        stopper.start();
    }

    private void quit() {
        System.exit(0);
    }
}
