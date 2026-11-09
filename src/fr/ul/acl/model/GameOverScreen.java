package fr.ul.acl.model;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JFrame {
    private ImageIcon dyingAnimation;

    public GameOverScreen(
            boolean won,
            long elapsedSeconds,
            Runnable restartAction) {

        dyingAnimation = new ImageIcon(
            getClass().getResource("/hero/dying.gif")
        );

        setTitle(won ? "Hachimi - Victory!" : "Hachimi - Game Over");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Ending message
        JLabel title = new JLabel(
            won ? "Congratulations! You won!" : "Game Over! You lost!",
            SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        // Elapsed time
        if(won){
            long minutes = elapsedSeconds / 60;
            long seconds = elapsedSeconds % 60;

            JLabel timeLabel = new JLabel(
                String.format("Time: %02d:%02d", minutes, seconds),
                SwingConstants.CENTER
            );
            timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            add(timeLabel, BorderLayout.CENTER);
        }
        else{
            JLabel heroLabel = new JLabel(dyingAnimation);
            heroLabel.setPreferredSize(new Dimension(20, 20));
            add(heroLabel);
        }

        // Restart button
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            dispose();
            restartAction.run();
        });

        // Quit button
        JButton closeButton = new JButton("Quit");
        closeButton.addActionListener(e -> System.exit(0));

        JPanel bottom = new JPanel();
        bottom.add(restartButton);
        bottom.add(closeButton);

        add(title, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);

        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}