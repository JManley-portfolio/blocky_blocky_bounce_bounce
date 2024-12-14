package com.example.blocky_blocky_v2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class BlockyApplication extends Application {
    Stage primary;
    @Override
    public void start(Stage primary) throws IOException {
        this.primary = primary;
            primary.setTitle("Blocky Blocky Bounce Bounce");
        //showGameScreenHard("Joel Manley");
        //showGameScreen("Joel Manley");
       showTitleScreen();
    }// END Primary

    public void showTitleScreen(){
        TitleScreen titleScreen = new TitleScreen(this.primary, this);
        titleScreen.show(primary);
    }
    public void showGameScreen(String playerName){
        GameScreen gameScreen = new GameScreen(this.primary, this, playerName, false);
        gameScreen.show(primary);
    }
    public void showGameScreenHard(String playerName){
        GameScreenHard gameScreenHard = new GameScreenHard(this.primary, this, playerName, true);
        gameScreenHard.show(primary);
    }
    public void showResultScreen(AnimationTimer timer, String playerName, int score, boolean hard){
        timer.stop();
        ResultScreen resultScreen = new ResultScreen(this.primary, this, playerName, score, hard);
        resultScreen.show(primary);
    }

    public static void main(String[] args) {
        launch();
    }
} // END Blocky Application