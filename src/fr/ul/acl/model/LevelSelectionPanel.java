
package fr.ul.acl.model;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class LevelSelectionPanel extends JPanel {

    private final List<GameLevel> levels;
    private final Consumer<GameLevel> onSelect;

    private int currentDifficulty = 1;

    private JLabel difficultyLabel;
    private JButton previousButton;
    private JButton nextButton;
    private JPanel levelGrid;

    private static final Dimension LEVEL_SIZE =
            new Dimension(130, 100);

    public LevelSelectionPanel(
            List<GameLevel> levels,
            Consumer<GameLevel> onSelect) {

        this.levels = levels;
        this.onSelect = onSelect;

        setLayout(new BorderLayout(15, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        createHeader();
        createContent();
        
        levels.get(0).unlock();
        updateLevels();
    }

    private void createHeader() {

        JLabel title = new JLabel(
                "HACHIMI - SELECT YOUR LEVEL",
                SwingConstants.CENTER);

        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        add(title, BorderLayout.NORTH);
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

        difficultyLabel = new JLabel(
                "",
                SwingConstants.CENTER);

        difficultyLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20));

        difficultyLabel.setPreferredSize(new Dimension(220, 30));

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

            JButton button = new JButton(
                    level.isLocked()
                            ? level.getName() + " 🔒"
                            : level.getName());

            button.setPreferredSize(new Dimension(110, 80));
            button.setMinimumSize(new Dimension(0, 80));

            button.setEnabled(!level.isLocked());

            button.addActionListener(
                    e -> onSelect.accept(level));

            gbc.gridx = column;
            gbc.gridy = row;

            levelGrid.add(button, gbc);

            column++;

            if (column == 3) {
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
}
