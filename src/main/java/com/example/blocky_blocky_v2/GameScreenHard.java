package com.example.blocky_blocky_v2;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.Iterator;
import java.util.Random;

import java.util.ArrayList;

public class GameScreenHard extends GameScreen {
    private ArrayList<Platform> fallingBlocks;
    private double dynamicBlockFallSpeed;

    public GameScreenHard(Stage stage, BlockyApplication blockyApp, String playerName, Boolean hardMode) {
        super(stage, blockyApp, playerName, hardMode);
        fallingBlocks = new ArrayList<>();
    }

    @Override
    public void show(Stage primary) {
        super.show(primary);
    }

    @Override
    protected void update(AnimationTimer refreshTimer, double deltaTime) {
        super.update(refreshTimer, deltaTime);
        //generate falling blocks
        generateBlocks();
        //apply gravity to falling blocks
        moveBlocks(deltaTime);
        //check collision on falling blocks
        detectCollisions();
    }

    @Override
    protected void draw(GraphicsContext gc) {
        super.draw(gc);
        gc.setFill(Color.RED);
        for (Platform block : fallingBlocks) {
            gc.fillRect(block.x - focus, block.y, block.width, block.height);
        }
    }

    private void generateBlocks() {
        if (rand.nextInt(500) < 5) { // every frame, small chance of block rendering
            //System.out.println("Creating Falling Block");
            double x = focus + rand.nextInt((int)(stage.getWidth()));
            double size = 30 + rand.nextInt(20);
            fallingBlocks.add(new Platform(x, 0, size, size));
        }
    }

    private void moveBlocks(double deltaTime) {
        dynamicBlockFallSpeed = (baseGravity * (stage.getHeight() / 900)) * .25;
        //dynamicBlockFallSpeed = gravity / 10;
        Iterator<Platform> iterator = fallingBlocks.iterator();
        while (iterator.hasNext()) {
            Platform block = iterator.next();
            block.fallingSpeed += dynamicBlockFallSpeed * (deltaTime * 60);
            block.y += block.fallingSpeed;

            if (block.y > stage.getHeight()) {
                iterator.remove();
            }
        }
    }

    private void detectCollisions() {
        for (Platform block : fallingBlocks) {
            double relativeBlockX = block.x - focus;
            boolean isUndersideCrash =
                    playerY >= block.y + block.height - block.fallingSpeed && // Adjust for falling speed
                    playerY + playerSpeedY <= block.y + block.height - block.fallingSpeed && // Adjust for falling speed
                    playerX + playerWidth > relativeBlockX &&
                    playerX < relativeBlockX + block.width;
            if (isUndersideCrash) {
                blockyApp.showResultScreen(refreshTimer, playerName, (int) score, hardMode);
            }

            boolean inVerticalRange =
                    playerY < block.y + block.height - block.fallingSpeed && // Adjust for falling speed
                    playerY + playerHeight > block.y;
            boolean onRightSide =
                    playerX <= relativeBlockX + block.width &&
                    playerX + playerWidth >= relativeBlockX + block.width;
            boolean onLeftSide =
                    playerX + playerWidth >= relativeBlockX &&
                    playerX <= relativeBlockX;
            if (inVerticalRange && onRightSide || inVerticalRange && onLeftSide) {
                blockyApp.showResultScreen(refreshTimer, playerName, (int) score, true);
            }
        }
    }
} // END GameScreenHard Class




