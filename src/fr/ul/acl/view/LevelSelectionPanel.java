
package fr.ul.acl.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import fr.ul.acl.model.GameLevel;

public class LevelSelectionPanel extends JPanel {

    private final List<GameLevel> levels;
    private final Consumer<GameLevel> onSelect;

    private int currentDifficulty = 1;

    private JLabel difficultyLabel;
    private JLabel starsLabel;
    private JButton previousButton;
    private JButton nextButton;
    private JButton settingsButton;
    private JPanel levelGrid;

    private static final Dimension LEVEL_SIZE =
            new Dimension(130, 100);

    public LevelSelectionPanel(
            List<GameLevel> levels,
            Consumer<GameLevel> onSelect,
            OptionsMenu settings) {

        this.levels = levels;
        this.onSelect = onSelect;

        currentDifficulty = GameLevel.LatestDifficulty;

        setLayout(new BorderLayout(15, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        createHeader(settings);
        createContent();

        updateLevels();
    }

    private void createHeader(OptionsMenu settings) {

        JPanel header = new JPanel(new BorderLayout());
        JPanel settingsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JLabel title = new JLabel(
                "SELECT YOUR LEVEL",
                SwingConstants.CENTER);

        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        settingsButton = new JButton("🛠");
        settingsButton.setToolTipText("Settings");
        settingsButton.addActionListener(e -> settings.setVisible(true));
        settingsRow.add(settingsButton);

        header.add(settingsRow, BorderLayout.NORTH);
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    private void createContent() {

        JPanel content = new JPanel(new BorderLayout(15, 20));

        createDifficultySelector(content);
        createLevelGrid(content);

        add(content, BorderLayout.CENTER);
    }

    private void createDifficultySelector(JPanel content) {

        JPanel selector = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10));

        previousButton = new JButton("◀");
        nextButton = new JButton("▶");

        difficultyLabel = new JLabel("", SwingConstants.CENTER);
        difficultyLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20));

        starsLabel = new JLabel("", SwingConstants.CENTER);
        starsLabel.setFont(
                new Font("SansSerif", Font.BOLD, 16));
        starsLabel.setForeground(new Color(220, 165, 30));

        previousButton.addActionListener(e -> {
            if (currentDifficulty > 1) {
                currentDifficulty--;
                updateLevels();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentDifficulty < GameLevel.DIFFICULTIES.length) {
                currentDifficulty++;
                updateLevels();
            }
        });

        selector.add(previousButton);
        selector.add(difficultyLabel);
        selector.add(starsLabel);
        selector.add(nextButton);

        content.add(selector, BorderLayout.NORTH);
    }
    
    private void createLevelGrid(JPanel content) {

        levelGrid = new JPanel(new GridBagLayout());

        levelGrid.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(levelGrid);

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        content.add(scrollPane, BorderLayout.CENTER);
    }

    private void updateLevels() {

        difficultyLabel.setText(
                GameLevel.DIFFICULTIES[currentDifficulty - 1]);

        previousButton.setEnabled(currentDifficulty > 1);

        nextButton.setEnabled(
                currentDifficulty < GameLevel.DIFFICULTIES.length);

        updateStars();

        levelGrid.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int column = 0;
        int row = 0;

        for (GameLevel level : levels) {

            if (level.getDifficulty() != currentDifficulty) {
                continue;
            }

            JPanel levelPanel = new JPanel(new BorderLayout(5, 5));
            levelPanel.setPreferredSize(LEVEL_SIZE);
            levelPanel.setBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1));

            JButton button = new JButton();
            button.setLayout(new GridBagLayout());
            button.setPreferredSize(LEVEL_SIZE);
            button.setEnabled(!level.isLocked());

            JPanel buttonContent = new JPanel();
            buttonContent.setLayout(new BoxLayout(buttonContent, BoxLayout.Y_AXIS));
            buttonContent.setOpaque(false);

            JLabel nameLabel = new JLabel(
                    level.isLocked() ? "🔒" : level.getName());

            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel stars = new JLabel(getStarsText(level.getStars()));
            stars.setFont(new Font("SansSerif", Font.BOLD, 17));
            stars.setForeground(new Color(220, 165, 30));
            stars.setAlignmentX(Component.CENTER_ALIGNMENT);

            buttonContent.add(nameLabel);

            if (!level.isLocked()) {
                buttonContent.add(Box.createVerticalStrut(2));
                buttonContent.add(stars);
            }

            button.add(buttonContent);

            button.addActionListener(e -> onSelect.accept(level));

            gbc.gridx = column;
            gbc.gridy = row;

            levelGrid.add(button, gbc);

            column++;

            if (column == 4) {
                column = 0;
                row++;
            }
        }

        // Keep the grid aligned to the top-left.
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 3;

        levelGrid.add(Box.createGlue(), gbc);

        levelGrid.revalidate();
        levelGrid.repaint();
    }

    private void updateStars() {

        int earnedStars = 0;
        int maxStars = 0;

        for (GameLevel level : levels) {
            if (level.getDifficulty() == currentDifficulty) {
                earnedStars += level.getStars();
                maxStars += 3;
            }
        }

        starsLabel.setText(
                "★ " + earnedStars + " / " + maxStars);
    }

    private String getStarsText(int stars) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            result.append(i < stars ? "★" : "☆");
        }

        return result.toString();
    }

    private void openSettings() {
        // Connect this to your existing settings/options panel.
    }
}
