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


/**
 * Represents the third level of the game, featuring a challenging boss fight.
 * <p>
 * <strong>Key Features:</strong>
 * <ul>
 *     <li>Introduces a boss enemy with dynamic health, shield, and position updates.</li>
 *     <li>Objective: Defeat the boss to complete the level.</li>
 *     <li>Custom UI elements for boss health and shield visibility.</li>
 * </ul>
 */

public class LevelThree extends LevelParent {

    /** Path to the background image used for Level Three. */
    protected static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/levelthreebg.gif";

    /** Initial health of the player in Level Three. */
    private static final int PLAYER_INITIAL_HEALTH = 8;

    /** The boss enemy featured in Level Three. */
    protected Boss boss;

    /** The specialized level view for Level Three, including boss-specific UI elements. */
    private LevelViewLevelTwo levelView;

    /**
     * Constructs a new instance of LevelThree.
     *
     * @param screenHeight the height of the game screen
     * @param screenWidth the width of the game screen
     * @param controller the controller managing game logic and transitions
     */

    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, controller); // Pass the controller
        boss = new Boss(this);
    }
    /**
     * Initializes the scene for Level Three, including objective text and animations.
     *
     * @return the initialized {@link Scene} for Level Three
     */

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

    /**
     * Initializes the friendly units for Level Three.
     * Adds the user plane to the scene's root group.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over based on the player's or boss's state.
     * <ul>
     *     <li>If the player's health is zero, the game ends with a loss.</li>
     *     <li>If the boss is defeated, the game ends with a win.</li>
     * </ul>
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss.isDestroyed()) {
            timeline.stop(); // Ensure the timeline stops
            // Remove the objective text
            getRoot().getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().contains("Objective"));
            winGame();
        }
    }

    /**
     * Spawns the boss enemy if no enemies are currently present.
     * Ensures the boss is added to the list of enemies for gameplay tracking.
     */
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

    /**
     * Updates the level view, including health bars, shields, and boss position.
     * Synchronizes the boss's shield visibility and position with the UI.
     */
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

    /**
     * Adjusts the position of the boss dynamically based on screen dimensions.
     * Ensures the boss is centered vertically and positioned near the right edge.
     */
    public void adjustBossPosition() {
        if (boss != null) {
            double bossX = getScreenWidth() * 0.9 - boss.getBoundsInLocal().getWidth() / 2; // 90% from the left edge
            double bossY = getScreenHeight() * 0.5 - boss.getBoundsInLocal().getHeight() / 2; // Centered vertically

            boss.setLayoutX(bossX);
            boss.setLayoutY(bossY);

            System.out.println("Boss position adjusted to: (" + bossX + ", " + bossY + ")");
        }
    }


    /**
     * Updates the position of the boss's shield relative to its location.
     *
     * @param bossX the X-coordinate of the boss
     * @param bossY the Y-coordinate of the boss
     */
public void updateBossShieldPosition(double bossX, double bossY) {
    levelView.updateShieldPosition(bossX, bossY);
}

    /**
     * Instantiates the level view for Level Three, including boss-specific UI elements.
     *
     * @return the {@link LevelViewLevelTwo} instance for Level Three
     */
    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

}
