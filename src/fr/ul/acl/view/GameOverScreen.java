
package fr.ul.acl.view;

import javax.swing.*;
import java.awt.*;
import fr.ul.acl.model.HachimiGame;

public class GameOverScreen extends JPanel {
    private static final int TIME_LIMIT = 120;
    private static final int HP_LIMIT = 60;

    private static final Dimension BUTTON_SIZE =
            new Dimension(160, 35);

    public GameOverScreen(
            HachimiGame game,
            boolean hasNextLevel,
            Runnable retryAction,
            Runnable nextLevelAction, 
            Runnable selectionAction,
            Runnable quitAction) {

        boolean won = game.hasWon();
        long time = game.getElapsedTime();
        int hp = game.getHero().getHealthPoints();

        boolean treasure = false;
        boolean timeEarned = time <= TIME_LIMIT;
        boolean hpEarned = hp >= HP_LIMIT;

        int stars = (treasure ? 1 : 0)
                + (timeEarned ? 1 : 0)
                + (hpEarned ? 1 : 0);

        game.getLevel().setStars(stars);

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(
                won ? "Congratulations! You won!" : "Game Over!",
                SwingConstants.CENTER);
        
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JPanel center = new JPanel(new BorderLayout(10, 10));

        if (won) {
                JPanel results = new JPanel();
                results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
                results.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

                JLabel starLabel = new JLabel(
                        "★".repeat(stars) + "☆".repeat(3 - stars),
                        SwingConstants.CENTER);

                starLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                starLabel.setForeground(new Color(220, 165, 30));
                starLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                results.add(starLabel);
                results.add(Box.createVerticalStrut(25));

                results.add(createCriterion(
                        "Completion time",
                        formatTime(time) + " / " + formatTime(TIME_LIMIT),
                        timeEarned
                ));

                results.add(Box.createVerticalStrut(15));

                results.add(createCriterion(
                        "Remaining HP",
                        hp + " / " + HP_LIMIT,
                        hpEarned
                ));

                results.add(Box.createVerticalStrut(15));

                results.add(createCriterion(
                        "Treasure",
                        "0 / 0",
                        treasure
                ));

                center.add(results, BorderLayout.CENTER);
        }
        else{
                ImageIcon animation = new ImageIcon(getClass().getResource("/lose.gif"));
                JLabel image = new JLabel(animation, SwingConstants.CENTER);
                center.add(image, BorderLayout.CENTER);
        }

        JPanel buttons = new JPanel(
                new GridLayout(2, 2, 12, 10));

        JButton retry = createButton("⟳", "Retry");
        retry.addActionListener(e -> retryAction.run());

        JButton next = createButton("▶", "Next Level");
        next.setEnabled(hasNextLevel);
        next.addActionListener(e -> nextLevelAction.run());

        JButton selection = createButton("⫶☰", "Level Selection");
        selection.addActionListener(e -> selectionAction.run());

        JButton quit = createButton("✖", "Quit");
        quit.addActionListener(e -> quitAction.run());

        buttons.add(retry);
        buttons.add(next);
        buttons.add(selection);
        buttons.add(quit);

        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JButton createButton(String icon, String text) {

        JButton button = new JButton(icon + "  " + text);

        button.setPreferredSize(BUTTON_SIZE);

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusable(false);
        button.setToolTipText(text);

        return button;
    }

    private String formatTime(long seconds) {
        return String.format("%d:%02d", seconds / 60,seconds % 60);
    }

    private JPanel createCriterion(
        String name,
        String result,
        boolean achieved) {

        JPanel row = new JPanel(new BorderLayout(20, 0));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel resultLabel = new JLabel(result, SwingConstants.RIGHT);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        resultLabel.setForeground(achieved
                ? new Color(40, 160, 80)
                : Color.GRAY);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(resultLabel, BorderLayout.EAST);

        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        return row;
    }
}
