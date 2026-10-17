package fr.ul.acl.model;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class OptionsMenu extends JFrame {

    public OptionsMenu(
            JFrame parent,
            HachimiController controller,
            MusicPlayer musicPlayer,
            Runnable onClose) {

        setTitle("Hachimi - Paramètres");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel settings = new JPanel();
        settings.setLayout(
            new BoxLayout(settings, BoxLayout.Y_AXIS)
        );

        settings.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        // Keyboard settings
        settings.add(new JLabel("Keyboard layout"));

        JRadioButton azertyButton =
            new JRadioButton("AZERTY (ZQSD)");

        JRadioButton qwertyButton =
            new JRadioButton("QWERTY (WASD)");

        ButtonGroup keyboardGroup = new ButtonGroup();
        keyboardGroup.add(azertyButton);
        keyboardGroup.add(qwertyButton);

        if (controller.getKeyboardLayout()
                == HachimiController.KeyboardLayout.AZERTY) {
            azertyButton.setSelected(true);
        } else {
            qwertyButton.setSelected(true);
        }

        azertyButton.addActionListener(e ->
            controller.setKeyboardLayout(
                HachimiController.KeyboardLayout.AZERTY
            )
        );

        qwertyButton.addActionListener(e ->
            controller.setKeyboardLayout(
                HachimiController.KeyboardLayout.QWERTY
            )
        );

        settings.add(azertyButton);
        settings.add(qwertyButton);
        settings.add(Box.createVerticalStrut(15));

        // Music settings
        JCheckBox muteButton =
            new JCheckBox("Mute background music");

        muteButton.setSelected(
            musicPlayer != null && musicPlayer.isMuted()
        );

        muteButton.setEnabled(musicPlayer != null);

        muteButton.addActionListener(e -> {
            if (musicPlayer != null) {
                musicPlayer.setMuted(muteButton.isSelected());
            }
        });

        settings.add(muteButton);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(closeButton);

        add(settings, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onClose.run();
            }
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}