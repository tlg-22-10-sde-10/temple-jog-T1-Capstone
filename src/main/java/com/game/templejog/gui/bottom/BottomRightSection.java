package com.game.templejog.gui.bottom;

import javax.swing.*;
import java.awt.*;

import static com.game.templejog.gui.MainContainer.MAIN_CONTAINER_HEIGHT;
import static com.game.templejog.gui.MainContainer.MAIN_CONTAINER_WIDTH;

public class BottomRightSection {
    private final JPanel bottomRightSectionJPanel;

    public BottomRightSection() {
        this.bottomRightSectionJPanel = new JPanel();
        bottomRightSectionJPanel.setSize(MAIN_CONTAINER_WIDTH / 2, MAIN_CONTAINER_HEIGHT / 6);
        bottomRightSectionJPanel.setLayout(new BoxLayout(bottomRightSectionJPanel, BoxLayout.Y_AXIS));
        bottomRightSectionJPanel.setBackground(new Color(129, 255, 217));
    }

    public JPanel getBottomRightSectionJPanel() {
        return bottomRightSectionJPanel;
    }
}
