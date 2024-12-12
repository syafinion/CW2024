package com.example.demo.levels;

import com.example.demo.actors.Boss;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;
import com.example.demo.controller.Controller;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LevelThree extends LevelParent {

    protected static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/levelthreebg.gif";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    protected Boss boss;
    private LevelViewLevelTwo levelView;

    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, controller); // Pass the controller
        boss = new Boss(this);
    }


    @Override
    public Scene initializeScene() {
        Scene scene = super.initializeScene(); // Call the base logic to set up the scene

        // Create objective text
        Text objectiveText = new Text("Objective: Defeat the Boss");
        objectiveText.setFont(Font.font("Press Start 2P", 20)); // Use the retro font
        objectiveText.setFill(Color.YELLOW);

        // Position the text below the health bar
        double objectiveTextX = getScreenWidth() / 2 - objectiveText.getLayoutBounds().getWidth() / 2;
        double objectiveTextY = getScreenHeight() * 0.12; // 12% from the top of the screen
        objectiveText.setLayoutX(objectiveTextX);
        objectiveText.setLayoutY(objectiveTextY);

        // Add the text to the root
        getRoot().getChildren().add(objectiveText);

        // Fade-in animation for the text
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), objectiveText);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        return scene;
    }


    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss.isDestroyed()) {
            // Remove the objective text
            getRoot().getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().contains("Objective"));
            winGame();
        }
    }


    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            if (boss == null) {
                boss = new Boss(this); // Pass the current level instance
                System.out.println("Boss initialized.");
            }
            addEnemyUnit(boss);
            System.out.println("Boss added to enemy units.");
        }
    }






    @Override
    protected void updateLevelView() {
        super.updateLevelView(); // Update hearts and other UI elements
        adjustBossPosition();
        if (boss != null) {
            levelView.updateBossHealthBar(boss.getHealth(), 100);

            // Sync shield position with the boss
            double bossX = boss.getLayoutX() + boss.getTranslateX();
            double bossY = boss.getLayoutY() + boss.getTranslateY();
            levelView.updateShieldPosition(bossX, bossY);

            // Handle shield visibility
            if (boss.isShielded()) {
                System.out.println("Shield should be visible.");
                levelView.showShield();
            } else {
                System.out.println("Shield should be hidden.");
                levelView.hideShield();
            }
        }
    }

    public void adjustBossPosition() {
        if (boss != null) {
            double bossX = getScreenWidth() * 0.9 - boss.getBoundsInLocal().getWidth() / 2; // 90% from the left edge
            double bossY = getScreenHeight() * 0.5 - boss.getBoundsInLocal().getHeight() / 2; // Centered vertically

            boss.setLayoutX(bossX);
            boss.setLayoutY(bossY);

            System.out.println("Boss position adjusted to: (" + bossX + ", " + bossY + ")");
        }
    }



public void updateBossShieldPosition(double bossX, double bossY) {
    levelView.updateShieldPosition(bossX, bossY);
}


    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

}
