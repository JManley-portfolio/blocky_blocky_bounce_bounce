package com.example.blocky_blocky_v2;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javafx.scene.control.Label;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class ResultScreen {
    private Stage stage;
    private BlockyApplication blockyApp;
    private String playerName;
    private int score;
    private boolean hard;

    public ResultScreen(Stage stage, BlockyApplication blockyApp, String playerName, int score, boolean hard) {
        this.stage = stage;
        this.blockyApp = blockyApp;
        this.playerName = playerName;
        this.score = score;
        this.hard = hard;
    }

    public void show(Stage primary) {
        stage.setTitle("Blocky Results Screen");
        stage.setWidth(800);
        stage.setHeight(600);

        VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(40); // Spacing between elements
            root.setPadding(new Insets(5));
            root.setStyle("-fx-background-color: DARKSLATEGREY;");

        Text resultTitle = new Text("Game Over!");
            resultTitle.setFont(Font.font("Stencil", 50));
            resultTitle.setFill(Color.LAVENDER);
            resultTitle.setWrappingWidth(350);
            resultTitle.setTextAlignment(TextAlignment.CENTER);

        Label playerInfo = new Label(playerName + "'s Score: " + score);
            playerInfo.setFont(Font.font("Impact", 20));
            playerInfo.setTextFill(Color.LAVENDER);

        VBox highScores = new VBox();
            highScores.setAlignment(Pos.CENTER);

        Path leaderboardPath = Path.of("leaderBoard.txt");
        if (hard) {leaderboardPath = Path.of("hardLeaderBoard.txt");}
        showLeaderboard(highScores, playerName, score, leaderboardPath);

        HBox buttonDisplay = new HBox();
        buttonDisplay.setAlignment(Pos.CENTER);
        buttonDisplay.setSpacing(50);

        Button playButton = new Button("Play Again");
            playButton.setStyle("""
                        -fx-background-color: LAVENDER;\s
                        -fx-text-fill: LIGHTSLATEGREY;\s
                        -fx-font-family: impact;\s
                        -fx-font-size: 24px;\s
            """);
        playButton.setOnAction(e -> {
            System.out.println(hard);
            if (hard) {
                System.out.println("Hard Mode");
                blockyApp.showGameScreenHard(playerName);
            } else {
                System.out.println("Easy Mode");
                blockyApp.showGameScreen(playerName);
            }
        });

        Button titleButton = new Button("Back to Title");
            titleButton.setStyle("""
                        -fx-background-color: LAVENDER;\s
                        -fx-text-fill: LIGHTSLATEGREY;\s
                        -fx-font-family: impact;
                        -fx-font-size: 24px;\s
            """);
        titleButton.setOnAction(event -> {blockyApp.showTitleScreen();});

        buttonDisplay.getChildren().addAll(playButton, titleButton);
        root.getChildren().addAll(resultTitle, playerInfo, highScores, buttonDisplay);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } // END Show

    private void showLeaderboard(VBox highScores, String playerName, int playerScore, Path filePath) {
        try {
            // Get all leaders from file, save them in useable format
            List<String> fileLines = Files.readAllLines(filePath); // Save all lines into list of strings
            List<Map.Entry<String, Integer>> leaders = new ArrayList<>(); // split into map with player name as key, score as value
            for (String line : fileLines) {
                String[] splitString = line.split(",");
                String leaderName = splitString[0];
                int leaderScore = Integer.parseInt(splitString[1]);
                leaders.add(Map.entry(leaderName, leaderScore));
            }
            // Add player to list
            leaders.add(Map.entry(playerName, playerScore));
            // sort list by score
            leaders.sort((leader1, leader2) -> leader2.getValue() - leader1.getValue());
            // if more than 5, remove bottom item
            if (leaders.size() > 5) {
                leaders.removeLast();
            }
            // display leader board
            Text leaderTitle = new Text("Leaderboard");
                leaderTitle.setFont(Font.font("Stencil", 30));
                leaderTitle.setFill(Color.LAVENDER);
                leaderTitle.setTextAlignment(TextAlignment.CENTER);
            highScores.getChildren().clear();
            highScores.getChildren().add(leaderTitle);
            highScores.setStyle("-fx-border-color: black");
            highScores.setPadding(new Insets(5));
            highScores.setSpacing(2);
            highScores.setMaxWidth(300);
            int count = 1;
            for (Map.Entry<String, Integer> leader: leaders) {
                HBox leaderContainer = new HBox();
                    leaderContainer.setPadding(new Insets(5, 2, 5, 2));
                Text nameText = new Text(count + ". " + leader.getKey());
                    nameText.setFont(Font.font("Impact", 20));
                    nameText.setFill(Color.LAVENDER);
                    count++;

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Text scoreText = new Text(Integer.toString(leader.getValue()));
                    scoreText.setFont(Font.font("Impact", 20));
                    scoreText.setFill(Color.LAVENDER);
                    scoreText.setTextAlignment(TextAlignment.RIGHT);

                leaderContainer.getChildren().addAll(nameText, spacer, scoreText);
                highScores.getChildren().add(leaderContainer);
            }
            // turn new leaderboard into correct format for .txt
            fileLines.clear();
            for (Map.Entry<String, Integer> leader: leaders) {
                fileLines.add(leader.getKey() + "," + leader.getValue());
            }
            // rewrite leaderBoard.txt with new, formatted leaderboard
            Files.write(filePath, fileLines, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // END Show leaderboard function
} // END TITLE CLASS
