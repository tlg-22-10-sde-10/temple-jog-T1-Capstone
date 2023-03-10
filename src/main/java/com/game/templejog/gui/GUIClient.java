package com.game.templejog.gui;

import com.game.templejog.Game;
import com.game.templejog.Sound;
import com.game.templejog.Temple;
import com.game.templejog.client.FileLoader;

import java.io.IOException;

public class GUIClient {

    public static void main(String[] args) throws IOException {
        Temple gameFiles = FileLoader.jsonLoader("JSON/gameFiles.json");
        Game game = new Game(gameFiles);
        RunGUI runGui = new RunGUI(game);
        TitleScreen sm = new TitleScreen();
        IntroScreen is = new IntroScreen(runGui);
        sm.gameStartScreen(game);
        Sound.themeSound("sounds/background_music.wav");
    }
}
