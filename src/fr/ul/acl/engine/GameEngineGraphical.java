
package fr.ul.acl.engine;

import javax.swing.SwingUtilities;
import fr.ul.acl.model.WinScreen;
import fr.ul.acl.model.HachimiGame;
import fr.ul.acl.model.HachimiController;

import java.io.IOException;

public class GameEngineGraphical {

    private Game game;
    private GamePainter gamePainter;
    private GameController gameController;
    private GraphicalInterface gui;

    public GameEngineGraphical(
            Game game,
            GamePainter gamePainter,
            GameController gameController) {

        this.game = game;
        this.gamePainter = gamePainter;
        this.gameController = gameController;
    }

    public void run() throws InterruptedException {

        // Création de l'interface graphique
        this.gui = new GraphicalInterface(
            this.gamePainter,
            this.gameController
        );

        // Lancement de la première partie
        playGame();
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
            new WinScreen(
                game.getElapsedSeconds(),
                this::restartGame
            ).setVisible(true);
        });
    }

    private void restartGame() {

        try {
            // Réinitialisation du jeu
            ((HachimiGame) game).recommencer();
            
            // reinitialisation de la commande du jeu 
            ((HachimiController) gameController).resetCommand();
            

            // Relancement de la boucle dans un nouveau thread
            Thread gameThread = new Thread(() -> {
                try {
                    playGame();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            gameThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

