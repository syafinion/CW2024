package com.example.demo.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    private MainMenu mainMenu;
    private Controller mockController;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Mock the Controller
        mockController = Mockito.mock(Controller.class);

        // Mock the stage returned by the controller
        Stage mockStage = Mockito.mock(Stage.class);
        Mockito.when(mockController.getStage()).thenReturn(mockStage);

        // Initialize MainMenu with the mocked Controller
        mainMenu = new MainMenu(mockController);
    }

    @Test
    void testCreateMenuScene() {
        Platform.runLater(() -> {
            // Create the menu scene
            Scene scene = mainMenu.createMenuScene();

            // Validate the scene and its root
            assertNotNull(scene, "Scene should not be null");
            assertNotNull(scene.getRoot(), "Scene root should not be null");

            // Ensure the root is of type Pane
            assertTrue(scene.getRoot() instanceof Pane, "Scene root should be of type Pane");
        });
    }

    @Test
    void testAddGlowEffect() {
        Platform.runLater(() -> {
            // Test the addGlowEffect method
            Text text = new Text("Test");
            mainMenu.addGlowEffect(text, Color.RED);

            // Ensure the text has a DropShadow effect
            assertNotNull(text.getEffect(), "Text should have an effect applied");
            assertTrue(text.getEffect() instanceof javafx.scene.effect.DropShadow, "Effect should be of type DropShadow");

            // Verify the color of the DropShadow
            javafx.scene.effect.DropShadow dropShadow = (javafx.scene.effect.DropShadow) text.getEffect();
            assertEquals(Color.RED, dropShadow.getColor(), "DropShadow color should be RED");
        });
    }

    @Test
    void testResolutionSetting() {
        Platform.runLater(() -> {
            mainMenu.createMenuScene();

            // Simulate setting a resolution
            assertDoesNotThrow(() -> mainMenu.setPendingResolution(1920, 1080, true));

            // Since resolution settings don't return a value, just ensure no exceptions are thrown
        });
    }

    @Test
    void testFullscreenToggle() {
        Platform.runLater(() -> {
            // Create the menu scene
            mainMenu.createMenuScene();

            // Test toggling fullscreen mode
            assertDoesNotThrow(() -> mainMenu.toggleFullscreen());
        });
    }
}
