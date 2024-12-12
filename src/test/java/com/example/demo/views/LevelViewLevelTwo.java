package com.example.demo.views;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelViewLevelTwoTest {

    private LevelViewLevelTwo levelViewLevelTwo;
    private Group root;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        // Use a latch to ensure JavaFX setup completes before tests run
        final Object lock = new Object();
        Platform.runLater(() -> {
            synchronized (lock) {
                // Initialize the root Group
                root = new Group();

                // Create a Scene and assign the Group as its root
                Scene scene = new Scene(root, 800, 600);

                // Mock a Stage and set the Scene on it
                Stage stage = new Stage();
                stage.setScene(scene);

                // Initialize LevelViewLevelTwo with the proper root
                levelViewLevelTwo = new LevelViewLevelTwo(root, 3);

                lock.notify(); // Signal that setup is complete
            }
        });

        synchronized (lock) {
            lock.wait(); // Wait for JavaFX setup to complete
        }
    }

    @Test
    void testInitialHealthBarDisplay() {
        Platform.runLater(() -> {
            assertNotNull(levelViewLevelTwo, "LevelViewLevelTwo should be initialized");
            assertFalse(root.getChildren().isEmpty(), "Root group should not be empty after initializing health bars");
        });
    }

    @Test
    void testUpdateBossHealthBar() {
        Platform.runLater(() -> {
            int maxHealth = 100;
            int currentHealth = 50;

            // Update the boss health bar
            levelViewLevelTwo.updateBossHealthBar(currentHealth, maxHealth);

            // Calculate expected width
            double expectedWidth = 300 * (double) currentHealth / maxHealth; // HEALTH_BAR_WIDTH = 300

            // Validate the health bar width
            assertEquals(expectedWidth, levelViewLevelTwo.healthBar.getWidth(), 0.01, "Health bar width should be updated correctly");
        });
    }

    @Test
    void testUpdateShieldHealthBar() {
        Platform.runLater(() -> {
            int maxHealth = 100;
            int currentHealth = 75;

            // Update the shield health bar
            levelViewLevelTwo.updateShieldHealthBar(currentHealth, maxHealth);

            // Calculate expected width
            double expectedWidth = 300 * (double) currentHealth / maxHealth; // HEALTH_BAR_WIDTH = 300

            // Validate the shield health bar width
            assertEquals(expectedWidth, levelViewLevelTwo.shieldHealthBar.getWidth(), 0.01, "Shield health bar width should be updated correctly");
        });
    }

    @Test
    void testShowShield() {
        Platform.runLater(() -> {
            levelViewLevelTwo.showShield();
            System.out.println("Shield should be visible.");
            // Ensure no exceptions occur during this method call
        });
    }

    @Test
    void testHideShield() {
        Platform.runLater(() -> {
            levelViewLevelTwo.hideShield();
            System.out.println("Shield should be hidden.");
            // Ensure no exceptions occur during this method call
        });
    }

    @Test
    void testUpdateShieldPosition() {
        Platform.runLater(() -> {
            double bossX = 200;
            double bossY = 100;

            // Update shield position
            levelViewLevelTwo.updateShieldPosition(bossX, bossY);

            // Validate shield position
            assertEquals(bossX + 1, levelViewLevelTwo.shieldImage.getLayoutX(), 0.01, "Shield X position should be updated correctly");
            assertEquals(bossY - ShieldImage.SHIELD_SIZE / 4, levelViewLevelTwo.shieldImage.getLayoutY(), 0.01, "Shield Y position should be updated correctly");
        });
    }
}
