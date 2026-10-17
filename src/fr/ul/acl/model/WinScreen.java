package fr.ul.acl.model;

import javax.swing.*;
import java.awt.*;

public class WinScreen extends JFrame {

    public WinScreen(long elapsedSeconds) {
        setTitle("Hachimi - Victoire !");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
            "Bravo ! Tu as gagné !",
            SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;

        JLabel timeLabel = new JLabel(
            String.format("Temps : %02d:%02d", minutes, seconds),
            SwingConstants.CENTER
        );
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JButton closeButton = new JButton("Quitter");
        closeButton.addActionListener(e -> {
            System.exit(0);
        });

        JPanel bottom = new JPanel();
        bottom.add(closeButton);

        add(title, BorderLayout.NORTH);
        add(timeLabel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(350, 180);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}