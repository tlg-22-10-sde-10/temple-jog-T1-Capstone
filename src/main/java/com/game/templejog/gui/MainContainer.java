package com.game.templejog.gui;

import com.game.templejog.Game;
import com.game.templejog.gui.bottom.BottomSection;
import com.game.templejog.gui.middle.MiddleSection;
import com.game.templejog.gui.top.TopHUD;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainContainer {
    public static final int MAIN_CONTAINER_WIDTH = 1200;
    public static final int MAIN_CONTAINER_HEIGHT = 900;
    private Game game;
    private static TopHUD topHUD;
    private static MiddleSection middleSection;
    private static BottomSection bottomSection;
    private JFrame mainContainer;

    public MainContainer(Game game, TopHUD topHUD, MiddleSection middleSection, BottomSection bottomSection) {
        // main container
        this.mainContainer = new JFrame();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setSize(MAIN_CONTAINER_WIDTH, MAIN_CONTAINER_HEIGHT);
        mainContainer.setLocationRelativeTo(null);
        this.game = game;
        MainContainer.topHUD = topHUD;
        MainContainer.middleSection = middleSection;
        MainContainer.bottomSection = bottomSection;
    }

    public void setUpMainContainer() {
        mainContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainContainer.add(topHUD.setUpTopHUDJPanel(), BorderLayout.PAGE_START);
        this.mainContainer.add(middleSection.setUpMiddleSectionJPanel(), BorderLayout.CENTER);
        this.mainContainer.add(bottomSection.setUpBottomSectionJPanel(), BorderLayout.PAGE_END);
        mainContainer.setResizable(false);
        mainContainer.setVisible(true);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static TopHUD getTopHUD() {
        return topHUD;
    }

    public static void setTopHUD(TopHUD t) {
        topHUD = t;
    }

    public static MiddleSection getMiddleSection() {
        return middleSection;
    }

    public void setMiddleSection(MiddleSection middleSection) {
        this.middleSection = middleSection;
    }

    public static BottomSection getBottomSection() {
        return bottomSection;
    }

    public void setBottomSection(BottomSection bottomSection) {
        MainContainer.bottomSection = bottomSection;
    }

    public JFrame getMainContainer() {
        return mainContainer;
    }

    public void setMainContainer(JFrame mainContainer) {
        this.mainContainer = mainContainer;
    }


}
