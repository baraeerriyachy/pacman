
package fr.ul.acl.engine;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import fr.ul.acl.model.*;

public class GraphicalInterface {

    private static final String SELECTION = "selection";
    private static final String GAME = "game";
    private static final String GAME_OVER = "game_over";

    private final JFrame frame;
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final HachimiController controller;

    private DrawingPanel drawingPanel;

    public GraphicalInterface(HachimiController controller) {

    this.controller = controller;

        frame = new JFrame("Hachimi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        frame.setContentPane(cards);
    }

    public void showLevelSelection(
            List<GameLevel> levels,
            Consumer<GameLevel> onSelect) {

        cards.removeAll();

        cards.add(
            new LevelSelectionPanel(levels, onSelect),
            SELECTION
        );

        cardLayout.show(cards, SELECTION);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public void showGame(
            GamePainter painter,
            GameController gameController,
            Runnable pauseAction,
            Runnable restartAction,
            Runnable levelAction,
            String levelName
            ) {

        drawingPanel = new DrawingPanel(painter);

        drawingPanel.addKeyListener(gameController);
        drawingPanel.setFocusable(true);

        JPanel container = new JPanel(new BorderLayout());
        container.add(drawingPanel, BorderLayout.CENTER);

        JButton pause = new JButton("❚❚");
        pause.setToolTipText("Pause");

        pause.addActionListener(e -> {
            pauseAction.run();

            boolean paused = pause.getToolTipText().equals("Pause");

            pause.setText(paused ? "▶" : "❚❚");
            pause.setToolTipText(paused ? "Reprendre" : "Pause");

            drawingPanel.requestFocusInWindow();
        });


        JButton restart = new JButton("⟳");
        restart.setToolTipText("Restart");
        restart.addActionListener(e -> restartAction.run());


        JButton level = new JButton("⫶☰");
        level.setToolTipText("Level Selection");
        level.addActionListener(e -> levelAction.run());


        JButton options = new JButton("🛠");
        options.setToolTipText("Settings");

        options.addActionListener(e -> {
            MusicPlayer music = controller.getMusicPlayer();

            OptionsMenu menu = new OptionsMenu(
                frame,
                controller,
                music,
                () -> drawingPanel.requestFocusInWindow()
            );

            menu.setVisible(true);
        });

        JPanel bottom = new JPanel(new BorderLayout());

        JLabel levelLabel = new JLabel(levelName);
        levelLabel.setBorder(
            BorderFactory.createEmptyBorder(0, 10, 0, 0)
        );

        // levelLabel.setFont(
        //     new Font("SansSerif", Font.BOLD, 18)
        // );

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        buttons.add(pause);
        buttons.add(restart);
        buttons.add(level);
        buttons.add(options);

        bottom.add(levelLabel, BorderLayout.WEST);
        bottom.add(buttons, BorderLayout.EAST);

        container.add(bottom, BorderLayout.SOUTH);

        cards.removeAll();

        cards.add(container, GAME);
        cardLayout.show(cards, GAME);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(
            () -> drawingPanel.requestFocusInWindow()
        );
    }

    public void showGameOver(GameOverScreen screen) {
        cards.removeAll();

        cards.add(screen, GAME_OVER);
        cardLayout.show(cards, GAME_OVER);

        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    public void paint() {
        if (drawingPanel != null) {
            drawingPanel.drawGame();
        }
    }

    public void close() {
        frame.dispose();
    }
}
