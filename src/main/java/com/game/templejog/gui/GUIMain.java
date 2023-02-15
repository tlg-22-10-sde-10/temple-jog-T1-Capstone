package com.game.templejog.gui;

import com.game.templejog.Game;
import com.game.templejog.Temple;
import com.game.templejog.client.FileLoader;
import com.game.templejog.gui.bottom.BottomSection;
import com.game.templejog.gui.middle.MiddleSection;
import com.game.templejog.gui.top.TopHUD;

import java.io.IOException;

public class GUIMain {
    public static void main(String[] args) throws IOException {
        Temple gameFiles = FileLoader.jsonLoader("JSON/gameFiles.json");
        Game game = new Game(gameFiles);
        TopHUD topHUD = new TopHUD(game);
        MiddleSection middleSection = new MiddleSection(game);
        BottomSection bottomSection = new BottomSection(game);
        MainContainer mainContainer = new MainContainer(game, topHUD, middleSection, bottomSection);
        mainContainer.setUpMainContainer();
        do {
            mainContainer.getMainContainer().repaint();
        } while (!game.getQuitGame()
                && game.getPlayer().getSteps() < 24
                && game.getPlayer().getHealth() > 0
                && !(game.getCommunicatorOff() && game.getCurrentRoom().getName().equalsIgnoreCase("landing zone")));
    }
}