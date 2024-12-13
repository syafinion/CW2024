package com.example.demo.views;

import com.example.demo.controller.Controller;
import com.example.demo.actors.UserPlane;
import com.example.demo.actors.Boss;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Manages the User Interface (UI) elements for each level.
 * <p>
 * This class is responsible for updating the level UI, displaying game over or win menus,
 * and managing countdown timers and health bars.
 * </p>
 */
public class LevelUIManager {

    private final double screenWidth;
    private final double screenHeight;
    private final Group root;
    private final Controller controller;

    /**
     * Constructs a new {@code LevelUIManager}.
     *
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param root the root {@link Group} for the level
     * @param controller the game {@link Controller}
     */
    public LevelUIManager(double screenWidth, double screenHeight, Group root, Controller controller) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.root = root;
        this.controller = controller;
    }

    /**
     * Updates the level UI by removing hearts, updating boss health bars, and handling shields.
     *
     * @param user the player's {@link UserPlane}
     * @param levelView the {@link LevelView} instance for the level
     * @param boss the boss {@link Boss} (if applicable)
     */

    public void updateLevelView(UserPlane user, LevelView levelView, Boss boss) {
        // Update the heart display
        levelView.removeHearts(user.getHealth());

        // Additional UI updates for LevelTwo (if applicable)
        if (levelView instanceof LevelViewLevelTwo && boss != null) {
            LevelViewLevelTwo levelTwoView = (LevelViewLevelTwo) levelView;
            levelTwoView.updateBossHealthBar(boss.getHealth(), 100);
            levelTwoView.updateShieldPosition(boss.getTranslateX(), boss.getTranslateY());
            if (boss.isShielded()) {
                levelTwoView.showShield();
            } else {
                levelTwoView.hideShield();
            }
        }
    }

    /**
     * Displays the game over menu with options to restart the level or go to the main menu.
     *
     * @param restartLevel the action to restart the current level
     * @param goToMainMenu the action to navigate to the main menu
     */
    public void showGameOverMenu(Runnable restartLevel, Runnable goToMainMenu) {
        // Create a pane for the game-over menu
        Pane gameOverPane = new Pane();
        gameOverPane.setPrefSize(screenWidth, screenHeight);

        // Add a semi-transparent black background
        Rectangle bg = new Rectangle(screenWidth, screenHeight);
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.7);
        gameOverPane.getChildren().add(bg);

        // Add the Game Over image
        GameOverImage gameOverImage = new GameOverImage(screenWidth, screenHeight);
        gameOverPane.getChildren().add(gameOverImage);

        // Retro font for buttons
        Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 20);

        // Create Restart and Main Menu buttons
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        // Restart button
        StackPane restartButton = createButton("RESTART", retroFont, restartLevel);
        // Main Menu button
        StackPane mainMenuButton = createButton("MAIN MENU", retroFont, goToMainMenu);

        menuBox.getChildren().addAll(restartButton, mainMenuButton);
        menuBox.setLayoutX((screenWidth - 220) / 2); // Center horizontally
        menuBox.setLayoutY(screenHeight / 2 + 40); // Position below the game-over image

        gameOverPane.getChildren().add(menuBox);

        // Add game-over menu to the root
        root.getChildren().add(gameOverPane);
    }

    /**
     * Displays the win menu with options to restart or go to the main menu.
     *
     * @param restartToLevelOne the action to restart from Level 1
     * @param goToMainMenu the action to navigate to the main menu
     */
    public void showWinMenu(Runnable restartToLevelOne, Runnable goToMainMenu) {
        Pane winPane = new Pane();
        winPane.setPrefSize(screenWidth, screenHeight);

        // Add a semi-transparent black background
        Rectangle bg = new Rectangle(screenWidth, screenHeight);
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.7);
        winPane.getChildren().add(bg);

        // Add the "You Win" image
        WinImage winImage = new WinImage(screenWidth, screenHeight);
        winPane.getChildren().add(winImage);

        Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 20);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        StackPane restartButton = createButton("RESTART", retroFont, restartToLevelOne);
        StackPane mainMenuButton = createButton("MAIN MENU", retroFont, goToMainMenu);

        menuBox.getChildren().addAll(restartButton, mainMenuButton);

        menuBox.setLayoutX((screenWidth - 220) / 2);
        menuBox.setLayoutY(screenHeight / 2 + 40);

        winPane.getChildren().add(menuBox);

        root.getChildren().add(winPane);
    }

    /**
     * Starts a countdown timer before the level begins.
     *
     * @param onComplete the action to run when the countdown ends
     * @param levelNumber the number of the level as a string
     */
    public void startCountdown(Runnable onComplete, String levelNumber) {
        Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 50);

        Text countdownText = new Text();
        countdownText.setFont(retroFont);
        countdownText.setFill(Color.WHITE);

        countdownText.setText("3");
        countdownText.setLayoutX((screenWidth - countdownText.getLayoutBounds().getWidth()) / 2);
        countdownText.setLayoutY(screenHeight / 2);

        root.getChildren().add(countdownText);

        Timeline countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> countdownText.setText("3")),
                new KeyFrame(Duration.seconds(1), e -> countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    countdownText.setText("Level " + levelNumber);
                    countdownText.setLayoutX((screenWidth - countdownText.getLayoutBounds().getWidth()) / 2);
                }),
                new KeyFrame(Duration.seconds(4), e -> {
                    root.getChildren().remove(countdownText);
                    onComplete.run();
                })
        );

        countdownTimeline.play();
    }

    private StackPane createButton(String name, Font font, Runnable action) {
        StackPane button = new StackPane();
        button.setPrefSize(220, 40);

        Rectangle bg = new Rectangle(220, 40);
        bg.setFill(Color.BLACK);
        bg.setStroke(Color.YELLOW);
        bg.setStrokeWidth(2);

        Text text = new Text(name);
        text.setFill(Color.YELLOW);
        text.setFont(font);

        button.getChildren().addAll(bg, text);

        button.setOnMouseEntered(e -> {
            bg.setFill(Color.DARKGRAY);
            text.setFill(Color.WHITE);
        });

        button.setOnMouseExited(e -> {
            bg.setFill(Color.BLACK);
            text.setFill(Color.YELLOW);
        });

        button.setOnMouseClicked(e -> action.run());
        return button;
    }
}
