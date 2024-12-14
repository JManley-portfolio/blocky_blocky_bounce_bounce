package com.example.blocky_blocky_v2;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TitleScreen {
    private Stage stage;
    private BlockyApplication blockyApp;

    public TitleScreen(Stage stage, BlockyApplication blockyApp) {
        this.stage = stage;
        this.blockyApp = blockyApp;
    }

    public void show(Stage primary) {
        stage.setTitle("Blocky Title Screen");
        stage.setWidth(800);
        stage.setHeight(600);

        VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(50); // Spacing between elements
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: darkslategrey;");

        Text titleText = new Text("Blocky Blocky Bounce Bounce");
            titleText.setFont(Font.font("Stencil", FontWeight.EXTRA_BOLD, 60));
            titleText.setFill(Color.LAVENDER);
            titleText.setWrappingWidth(500);
            titleText.setTextAlignment(TextAlignment.CENTER);

        HBox nameDisplay = new HBox();
            nameDisplay.setAlignment(Pos.CENTER);
            nameDisplay.setSpacing(50);

        Label nameLabel = new Label("Enter Player Name: ");
            nameLabel.setFont(Font.font("Impact", 20));
            nameLabel.setTextFill(Color.LAVENDER);

        TextField nameInput = new TextField();
            nameInput.setMaxWidth(250);
            nameInput.setMaxHeight(1);
            nameInput.setStyle("""
                    -fx-control-inner-background: LAVENDER;
                    -fx-text-fill: darkslategrey;
                    -fx-font-family: Impact;
                    -fx-font-size: 20px;
               \s""");

        nameDisplay.getChildren().addAll(nameLabel, nameInput);

        HBox buttonDisplay = new HBox();
            buttonDisplay.setAlignment(Pos.CENTER);
            buttonDisplay.setSpacing(50);

        Label entryError = new Label();
        entryError.setFont(Font.font("Impact", 20));
        entryError.setVisible(false);

        Button playButton = new Button("Play");
            playButton.setStyle("""
                    -fx-background-color: LAVENDER;\s
                    -fx-text-fill: lightslategrey;\s
                    -fx-font-family: Impact;\s
                    -fx-font-size: 24px;
       \s""");
            playButton.setPrefSize(200, 100);

            // Check for input and save player name
            playButton.setOnAction(event -> {
                String playerName = nameInput.getText().trim();
                if (playerName.isEmpty()) {
                    entryError.setText("You must enter a player name");
                    entryError.setVisible(true);
                } else {
                    entryError.setVisible(false);
                    blockyApp.showGameScreen(playerName);
                }
            });

        Button playHardButton = new Button("Hard Mode");
        playHardButton.setStyle("""
                    -fx-background-color: LAVENDER;\s
                    -fx-text-fill: lightslategrey;\s
                    -fx-font-family: Impact;\s
                    -fx-font-size: 24px;
       \s""");
        playHardButton.setPrefSize(200, 100);

        // Check for input and save player name
        playHardButton.setOnAction(event -> {
            String playerName = nameInput.getText().trim();
            if (playerName.isEmpty()) {
                entryError.setText("You must enter a player name");
                entryError.setVisible(true);
            } else {
                entryError.setVisible(false);
                blockyApp.showGameScreenHard(playerName);
            }
        });

        Button quitButton = new Button("Quit");
            quitButton.setStyle("""
                    -fx-background-color: LAVENDER;\s
                    -fx-text-fill: LIGHTSLATEGREY;\s
                    -fx-font-family: Impact;
                    -fx-font-size: 24px;
            \s""");
            quitButton.setPrefSize(200, 100);
            quitButton.setOnAction(event -> {Platform.exit();});

        buttonDisplay.getChildren().addAll(playButton, playHardButton, quitButton);
        root.getChildren().addAll(titleText, nameDisplay, entryError, buttonDisplay);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }// END Title Stage
}// END TITLE CLASS
