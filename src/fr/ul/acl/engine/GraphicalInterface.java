package fr.ul.acl.engine;

import javax.swing.*;
import java.awt.*;

import fr.ul.acl.model.HachimiController;
import fr.ul.acl.model.MusicPlayer;
import fr.ul.acl.model.OptionsMenu;

public class GraphicalInterface {

    private DrawingPanel panel;
    private JFrame frame;

    public GraphicalInterface(
            GamePainter gamePainter,
            GameController gameController) {

        frame = new JFrame("Hachimi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new DrawingPanel(gamePainter);
        panel.addKeyListener(gameController);
        panel.setFocusable(true);

        JButton optionsButton = new JButton("Paramètres");

        optionsButton.addActionListener(e -> {

			HachimiController controller =
				(HachimiController) gameController;

			MusicPlayer musicPlayer =
				controller.getMusicPlayer();

			OptionsMenu menu = new OptionsMenu(
				frame,
				controller,
				musicPlayer,
				() -> {
					panel.requestFocusInWindow();
				}
			);

			menu.setVisible(true);

        });

		JPanel container = new JPanel(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(optionsButton);

		container.add(bottomPanel, BorderLayout.SOUTH);
		frame.setContentPane(container);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }

    public void paint() {
        panel.drawGame();
    }
}