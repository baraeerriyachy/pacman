
package fr.ul.acl.engine;

import javax.swing.SwingUtilities;
import fr.ul.acl.model.GameOverScreen;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.HachimiController;

import java.io.IOException;

public class GameEngineGraphical {

    private Game game; 
    private GamePainter gamePainter;
    private GameController gameController;
    private GraphicalInterface gui;
    private Thread gameThread;

    public GameEngineGraphical(
            Game game,
            GamePainter gamePainter,
            GameController gameController) {

        this.game = game;
        this.gamePainter = gamePainter;
        this.gameController = gameController;
    }

    private void startGameThread() {
        gameThread = new Thread(() -> {
            try {
                playGame();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        gameThread.start();
    }

    public void run() throws InterruptedException {

        // Création de l'interface graphique
        this.gui = new GraphicalInterface(
            this.gamePainter,
            this.gameController,
            this::restartGame
        );

        // Lancement de la première partie
        startGameThread();
    }

    private void playGame() throws InterruptedException {

        while (!this.game.isFinished()) {

            Cmd c = this.gameController.getCommand();

            this.game.evolve(c);

            this.gui.paint();

            Thread.sleep(100);
        }

        // Affichage de la fenêtre de victoire
        SwingUtilities.invokeLater(() -> {
            new GameOverScreen(
                game.hasWon(),
                game.getElapsedSeconds(),
                this::restartGame
            ).setVisible(true);
        });
    }

    private void restartGame() {
        try {
            // Stop the previous game loop
            if (gameThread != null) {
                gameThread.interrupt();
            }

            // Reset game state
            ((HachimiGame) game).recommencer();
            ((HachimiController) gameController).resetCommand();

            // Refresh display
            gui.paint();

            // Start a fresh game loop
            startGameThread();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

