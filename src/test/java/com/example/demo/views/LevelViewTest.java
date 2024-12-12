package com.example.demo.views;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelViewTest {

    private LevelView levelView;
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

                // Initialize LevelView with the proper root
                levelView = new LevelView(root, 3, false);

                lock.notify(); // Signal that setup is complete
            }
        });

        synchronized (lock) {
            lock.wait(); // Wait for JavaFX setup to complete
        }
    }

    @Test
    void testInitialHeartDisplay() {
        Platform.runLater(() -> {
            assertNotNull(levelView, "LevelView should be initialized");

            // Test heart display is initialized with the correct number of hearts
            levelView.showHeartDisplay();
            assertFalse(root.getChildren().isEmpty(), "Root group should not be empty after adding heart display");
        });
    }

    @Test
    void testIsNotLevelThree() {
        Platform.runLater(() -> {
            assertFalse(levelView.isLevelThree(), "LevelView should not be LevelThree when initialized with false");
        });
    }

    @Test
    void testKillCountDisplayInitialValue() {
        Platform.runLater(() -> {
            assertNotNull(levelView.killCountDisplay, "Kill count display should be initialized");
            assertEquals("Kills: 0", levelView.killCountDisplay.getText(), "Initial kill count should be '0'");
        });
    }
}
