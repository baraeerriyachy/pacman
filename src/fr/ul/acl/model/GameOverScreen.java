
package fr.ul.acl.model;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JPanel {

    private static final Dimension BUTTON_SIZE =
            new Dimension(160, 35);

    public GameOverScreen(
            boolean won,
            boolean hasNextLevel,
            Runnable retryAction,
            Runnable nextLevelAction,
            Runnable selectionAction,
            Runnable quitAction) {

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(
                won ? "Congratulations! You won!" : "Game Over!",
                SwingConstants.CENTER);

        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        ImageIcon animation = new ImageIcon(
                getClass().getResource(
                        won ? "/victory.gif" : "/lose.gif"));

        JLabel image = new JLabel(animation, SwingConstants.CENTER);

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
        add(image, BorderLayout.CENTER);
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
}
