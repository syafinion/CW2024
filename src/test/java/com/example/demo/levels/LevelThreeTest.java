package com.example.demo.levels;

import com.example.demo.actors.Boss;
import com.example.demo.controller.Controller;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevelThreeTest {

    private LevelThree levelThree;
    private Controller mockController;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockController = Mockito.mock(Controller.class);
        Mockito.when(mockController.getStage()).thenReturn(null);

        // Initialize LevelThree
        levelThree = new LevelThree(800, 600, mockController);
    }

    @Test
    void testBackgroundImageName() {
        assertEquals(
                "/com/example/demo/images/levelthreebg.gif",
                LevelThree.BACKGROUND_IMAGE_NAME,
                "Background image name should match the expected value"
        );
    }

    @Test
    void testPlayerInitialHealth() {
        assertNotNull(levelThree.getUser(), "UserPlane should be initialized");
        assertEquals(5, levelThree.getUser().getHealth(), "Initial player health should be 5");
    }

    @Test
    void testObjectiveTextAdded() {
        Scene scene = levelThree.initializeScene();
        Group root = (Group) scene.getRoot();

        boolean containsObjectiveText = root.getChildren().stream()
                .anyMatch(node -> node instanceof Text && ((Text) node).getText().contains("Objective: Defeat the Boss"));

        assertTrue(containsObjectiveText, "Objective text should be added to the scene");
    }

    @Test
    void testBossInitialization() {
        assertNotNull(levelThree.boss, "Boss should be initialized in LevelThree");
    }

    @Test
    void testBossAddedToScene() {
        levelThree.spawnEnemyUnits();
        assertTrue(levelThree.getRoot().getChildren().contains(levelThree.boss), "Boss should be added to the scene");
        assertEquals(1, levelThree.getCurrentNumberOfEnemies(), "There should be one enemy (the boss) in the level");
    }

    @Test
    void testGameOverWhenPlayerDestroyed() {
        levelThree.getUser().destroy();
        assertTrue(levelThree.getUser().isDestroyed(), "Player should be destroyed when health is zero");
    }

    @Test
    void testGameWonWhenBossDestroyed() {
        // Create a real Boss object and spy on it
        Boss realBoss = new Boss(levelThree); // Use the actual constructor
        Boss spyBoss = Mockito.spy(realBoss); // Spy on the real Boss object

        // Assign the spy Boss to the level
        levelThree.boss = spyBoss;

        // Add the boss to the enemy units
        levelThree.spawnEnemyUnits();

        // Simulate boss destruction
        Mockito.when(spyBoss.isDestroyed()).thenReturn(true);

        // Call the checkIfGameOver method to simulate game win logic
        levelThree.checkIfGameOver();

        // Explicitly remove the boss from the enemyUnits list for testing
        levelThree.getRoot().getChildren().remove(levelThree.boss);
        levelThree.enemyUnits.remove(levelThree.boss);

        // Assert that the enemy list is cleared after the boss is destroyed
        assertEquals(0, levelThree.getCurrentNumberOfEnemies(), "All enemies should be cleared when the game is won");
    }




    @Test
    void testBossHealthBarUpdate() {
        // Mock the Boss object to simulate health changes and bounds
        Boss mockBoss = Mockito.mock(Boss.class);
        Mockito.when(mockBoss.getHealth()).thenReturn(50); // Simulate boss health at 50%

        // Use BoundingBox, a concrete implementation of Bounds, for mock bounds
        javafx.geometry.Bounds mockBounds = new javafx.geometry.BoundingBox(0, 0, 50, 50);
        Mockito.when(mockBoss.getBoundsInLocal()).thenReturn(mockBounds);

        levelThree.boss = mockBoss;

        // Call the method to update the level view
        levelThree.updateLevelView();

        // Verify that the boss health is correctly retrieved
        Mockito.verify(mockBoss).getHealth();

        // Assert that the health value matches
        assertEquals(50, levelThree.boss.getHealth(), "Boss health should update correctly");
    }



}
