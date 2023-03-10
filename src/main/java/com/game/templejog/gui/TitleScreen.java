package com.game.templejog.gui;

import com.game.templejog.Game;
import com.game.templejog.Sound;
import com.game.templejog.gui.top.QuitMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TitleScreen {

    JFrame startWindow;
    JPanel titleNamePanel, startButtonPanel, buttonPanel, bgPanel;
    JButton startButton, quitButton, settingsButton, creditsButton, loadButton;

    private boolean gameStarted = false;

    public TitleScreen() {
    }

    private static void saveGame(Game game) {
        try {
            FileOutputStream fos = new FileOutputStream("TempleJog.sav");
            ObjectOutput oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.flush();
            oos.close();
            System.out.println("Game saved\n");
        } catch (Exception e) {
            System.out.println("Serialization error: " + e.getClass() + ": " + e.getMessage());
        }
    }

    private static void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("TempleJog.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Game game = (Game) ois.readObject();
            ois.close();
            System.out.println("\n Game loaded");
        } catch (Exception e) {
            System.out.println("Deserialization error: " + e.getClass() + ": " + e.getMessage());
        }
    }

    public static void creditMenu() {
        JFrame creditFrame = new JFrame("Developers Menu");
        JTextArea creditMessage = new JTextArea("Temple Jog Capstone\n\nDevelopers:\nBryce Meadors, Joe Savella, Cindy Pottin\n\nAdaptation from Text Based Game Developed by:\nJoe Racke, Lorenzo Ortega, and Lok Tamang");
        creditMessage.setEditable(false);
        JOptionPane.showMessageDialog(creditFrame, creditMessage, "Developers", JOptionPane.INFORMATION_MESSAGE);
    }

    public void gameStartScreen(Game game) {
        //create JFrame to hold start menu and title
        startWindow = new JFrame();
        startWindow.setSize(800, 800);
        startWindow.setLocationRelativeTo(null);
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setLayout(null);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setSize(800, 800);
        backgroundPanel.setBackground(new Color(5, 23, 38));


        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(100, 0, 600, 500);
        ImageIcon bgIcon = null;
        try {
            //noinspection ConstantConditions
            bgIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("Images/temple-jog-Alien_green.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageLabel.setIcon(bgIcon);
        imageLabel.setBackground(new Color(5, 23, 38));

        startWindow.add(imageLabel);

        //Create Title
        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 10, 600, 60);
        titleNamePanel.setBackground(new Color(5, 23, 38));

        //create Start button Panel
        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(300, 500, 200, 75);
        startButtonPanel.setBackground(new Color(5, 23, 38));

        //create start button
        startButton = new JButton("START");
        startButton.setBackground(Color.white);
        startButton.setLocation(100, 100);
        /*DONE: connect to start game loop to call start of game show you are at LZ*/
        startButton.addActionListener(e -> {
            gameStarted = true;

            if (e.getSource() == this.startButton) {
                IntroScreen.gameIntroScreen(game);
                startWindow.dispose();
            }
        });

//        create load button
        loadButton = new JButton("LOAD");
        loadButton.setBackground(Color.white);
        loadButton.addActionListener(e -> {
            if (e.getSource() == this.loadButton) {
                loadGame();
            }
        });

        // create difficulty options button
        settingsButton = new JButton("SETTINGS");
        JFrame difficultyFrame = new JFrame("SETTINGS");
        difficultyFrame.setLayout(new FlowLayout());
        difficultyFrame.setSize(200, 200);
        difficultyFrame.setLocationRelativeTo(null);
        difficultyFrame.setResizable(false);
        difficultyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton medium = new JButton("I know what I'm doing!");
        medium.addActionListener(e -> {
            game.processDifficulty("medium");
            difficultyFrame.dispose();
        });
        JButton hard = new JButton("I really like a challenge!");
        hard.addActionListener(e -> {
            game.processDifficulty("hard");
            difficultyFrame.dispose();
        });
        JButton musicToggle = new JButton("Toggle Sound");
        musicToggle.addActionListener(e -> {
            if (game.getPlaySound()) {
                game.setPlaySound(false);
                Sound.stopSound();
            } else {
                if (gameStarted) {
                    game.setPlaySound(true);
                    Sound.themeSound(game.getCurrentRoom().getSound());
                } else {
                    game.setPlaySound(true);
                    Sound.themeSound("sounds/background_music.wav");
                }
            }
        });
        JLabel difficultyLabel = new JLabel("Select a difficulty");
        JLabel musicLabel = new JLabel("Music/SFX Settings");
        difficultyFrame.add(difficultyLabel);
        difficultyFrame.add(medium);
        difficultyFrame.add(hard);
        difficultyFrame.add(musicLabel);
        difficultyFrame.add(musicToggle);
        settingsButton.addActionListener(e -> difficultyFrame.setVisible(true));


        //create Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setBounds(300, 600, 200, 75);
        buttonPanel.setBackground(new Color(5, 23, 38));


        //create exit button
        quitButton = new JButton("EXIT");
        quitButton.setBackground(Color.white);

        /*DONE: add action listener to exit*/
        quitButton.addActionListener(e -> {
            if (e.getSource() == this.quitButton) {
                QuitMenu.setUpTitleScreenQuitOptions();
            }
        });

        //create credits button
        creditsButton = new JButton("CREDITS");
        creditsButton.setBackground(Color.white);
        /*DONE: add dialog information window with credit for codebase and our work*/
        creditsButton.addActionListener(e -> {
            if (e.getSource() == this.creditsButton) {
                creditMenu();
            }
        });

        startButtonPanel.add(startButton);
        startButtonPanel.add(loadButton);
        startButtonPanel.add(settingsButton);

        buttonPanel.add(creditsButton);
        buttonPanel.add(quitButton);

        startWindow.add(titleNamePanel);

        startWindow.add(startButtonPanel);
        startWindow.add(buttonPanel);
        startWindow.add(backgroundPanel);
        startWindow.setResizable(false);
        startWindow.setVisible(true);
    }

}
