package com.example.blocky_blocky_v2;

import com.example.blocky_blocky_v2.Platform;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen {
    protected BlockyApplication blockyApp;
    protected Stage stage;
    protected Random rand = new Random();
    protected AnimationTimer refreshTimer;
    protected Boolean hardMode;

    private ArrayList<Platform> platforms; // hold all created platforms
    private double lastPlatformX = 0; // track position of last platform rendered
    private double maxPlatformY;
    private double minPlatformY;

    protected String playerName;
    protected final int playerWidth = 75; // player block size
    protected final int playerHeight = 75;
    protected double playerX = 100;// initial player position
    protected double playerY = 100;
    protected double score;

    protected double focus = 0; // Keep focus on player block to allow "scrolling"
    protected double baseGravity = 0.3;
    protected double gravity;
    private double baseJumpPower = 30;
    private double basePlayerSpeed = 4;
    private double dynamicJumpPower;
    private double dynamicPlayerSpeed;

    protected double playerSpeedX = 0; // handle player sideways speed
    protected double playerSpeedY = 0; // handle vertical player speed

    //Control of frame rate CHANGES HERE
    protected long lastTime = System.nanoTime();
    protected final double frameTime = 1/60; // target 60fps

    public GameScreen(Stage stage, BlockyApplication blockyApp, String playerName, Boolean hardMode) {
        this.stage = stage;
        this.blockyApp = blockyApp;
        this.playerName = playerName;
        platforms = new ArrayList<>();
        this.hardMode = hardMode;
    }

    public void show(Stage primary) {
        stage.setTitle("Blocky Game Screen");
        stage.setWidth(1800);
        stage.setHeight(900);
        stage.setY(100);
        stage.setX(100);
        Canvas root = new Canvas(1800, 900);
        GraphicsContext gc = root.getGraphicsContext2D();

        /* CHANGES HERE TO MAKE GRAVITY CONSTANT */

        gravity = 1.5; // adjust gravity with base value, dependent on screen size
        dynamicJumpPower = baseJumpPower * (stage.getHeight() / 900);
        dynamicPlayerSpeed = basePlayerSpeed * (stage.getWidth() / 1800);
        maxPlatformY = stage.getHeight() * 7 / 8; // keep platform in bottom 7/8 of screen
        minPlatformY = stage.getHeight() / 8; // keep platform in top 7/8 of screen

        Scene gameScene = new Scene(new StackPane(root), 1800, 900);
            gameScene.setOnKeyPressed(this::handleKeyPressed);
            gameScene.setOnKeyReleased(this::handleKeyReleased);

        // Initial platform for player to start on
        platforms.add(new Platform(0, 700, 250, 40));
        lastPlatformX = 250;

        // ANIMATION TIMER
        refreshTimer = new AnimationTimer() {
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1000000000.0; // calculate frametime in seconds
                lastTime = now; // reset previous frame time

                if (deltaTime >= frameTime) { // only update and redraw game when enough time has passed
                    update(refreshTimer, deltaTime);
                    draw(gc);
                }
            }
        };
        refreshTimer.start();
        stage.setScene(gameScene);
        stage.show();
    } // END Game stage

    // handleKeyPressed function
    private void handleKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case SPACE, UP, W:
                playerSpeedY = -(dynamicJumpPower);
                break;
            case LEFT, A:
                playerSpeedX = -(dynamicPlayerSpeed);
                break;
            case RIGHT, D:
                playerSpeedX = dynamicPlayerSpeed;
                break;
        }
    }// End handleKeyPressed Function

    // handleKeyReleased function
    private void handleKeyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT, RIGHT, A, D -> playerSpeedX = 0;
        }
    } // End handleKeyReleased Function

    // update function
    protected void update(AnimationTimer refreshTimer, double deltaTime) { // CHANGES HERE
        // generate new platforms
        generatePlatforms();
        // remove off-screen platforms
        removeOffScreenPlatforms();

        // update player position and speed based on delta time CHANGES HERE
        playerX += playerSpeedX * (deltaTime * 60);
        playerY += playerSpeedY * (deltaTime * 60);
        playerSpeedY += gravity * (deltaTime * 60);

        //Move focus with player
        double screenWidth = stage.getWidth();
        if (playerX < screenWidth / 2) {
            focus += playerSpeedX;
        } else {
            playerX = screenWidth / 2;
            focus += playerSpeedX;
        }
        if (focus < 0) {
            playerX = 0;
        }
        collisionDetection();
        //keep score updated without letting it go down if player moves left
        score = Math.max(score, focus);

        /*
        if (playerY >= 800) { // testing bit, keeps player from falling off screen
            playerY = 800;
            playerSpeedY = 0;
        } */
        // Check for bounds to end game

        if (playerY + playerSpeedY >= stage.getHeight() || playerY + playerSpeedY + playerHeight < 0){
            blockyApp.showResultScreen(refreshTimer, playerName, (int)score, hardMode);
        }
    }// End Update function

    // draw function
    protected void draw(GraphicsContext gc) {
        // render page
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, 2400, 1200);

        // render score
        double centerX = stage.getWidth() / 2 - 10;
        double topY = stage.getHeight() / 5;
        gc.setFill(Color.LAVENDER);
        gc.setFont(Font.font("Impact", 24));
        gc.fillText("Score: " + (int)score, centerX, topY, 500);

        //render player block
        gc.setFill(Color.LAVENDER);
        gc.fillRect(playerX, playerY, playerWidth, playerHeight);

        //render platforms
        gc.setFill(Color.LIGHTCYAN);
        for (Platform platform : platforms) {
            gc.fillRect(platform.x - focus, platform.y, platform.width, platform.height);
        }
    }// End Draw Function

    private void generatePlatforms() {
        if (platforms.size() < 5) { // if no platform, or if the position of the last platform minus the focus position is less than the threshold
            double x = lastPlatformX + rand.nextInt(400);
            double y = minPlatformY + rand.nextInt((int)(maxPlatformY) - (int)(minPlatformY)); // random height // CHANGE HERE TO RANDOMIZE PLATFORMS FURTHER
            double width = 100 + rand.nextInt(100); //random width
            double height = 40 + rand.nextInt(300); //random height

            //System.out.println("Generating Platform at X: " + x + ", Y: " + y); // TESTING LINE
            platforms.add(new Platform(x, y, width, height)); // add platform to arraylist
            lastPlatformX = x + width; // update last platform position
        }
    }// End generatePlatforms Function

    private void removeOffScreenPlatforms() {
        Iterator<Platform> iterator = platforms.iterator();
        while (iterator.hasNext()) {
            Platform platform = iterator.next();
            if (platform.x - focus  + platform.width < 0) {// if end point of the platform is no longer in view
                iterator.remove(); // remove platform
            }
        }
    } // End removeOffScreenPlatforms function

    private void collisionDetection() {
        for (Platform platform : platforms) {
            double relativePlatformX = platform.x - focus;

            boolean islanding =
                    playerY + playerHeight <= platform.y && // player bottom at or below platform top
                    playerY + playerHeight + playerSpeedY >= platform.y && // using speed to prevent clipping through platforms
                    playerX + playerWidth > relativePlatformX && // player right edge within bounds
                    playerX < relativePlatformX + platform.width // player left edge within bounds
            ;
            if (islanding) {
                playerY = platform.y - playerHeight;
                playerSpeedY = 0;
            }

            boolean isUndersideCrash =
                    playerSpeedY < 0 &&
                    playerY >= platform.y + platform.height && // player top edge at or below platform bottom edge
                    playerY + playerSpeedY <= platform.y + platform.height && // player moving up into platform bottom
                    playerX + playerWidth > relativePlatformX && // player right edge past platform left edge
                    playerX < relativePlatformX + platform.width // player left edge past platform right edge
            ;
            if (isUndersideCrash) {
                playerY = platform.y + platform.height;
                playerSpeedY = gravity;
                return; // return to prevent user being moved to side
            }

            boolean inVerticalRange =
                    playerY < platform.y + platform.height && // player top edge above platform bottom edge
                    playerY + playerHeight > platform.y // player bottom edge below platform top edge
            ;
            boolean onRightSide =
                    playerX <= relativePlatformX + platform.width && // players left edge before platform left edge
                    playerX + playerWidth >= relativePlatformX + platform.width // players right edge is past platform right edge
            ;
            boolean onLeftSide =
                    playerX + playerWidth >= relativePlatformX && // players right edge is past platform left edge
                    playerX <= relativePlatformX // players left edge is before platform left edge
            ;
            if (inVerticalRange){
                if (onRightSide) {
                   playerX = relativePlatformX + platform.width;
                    playerSpeedX = 0;
                } else if (onLeftSide) {
                    playerX = relativePlatformX - playerWidth;
                    playerSpeedX = 0;
                }
            }
        }
    } // END collision detection function
} // END GAME CLASS
