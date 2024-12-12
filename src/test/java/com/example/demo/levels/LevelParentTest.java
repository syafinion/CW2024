package com.example.demo.levels;

import com.example.demo.actors.UserPlane;
import com.example.demo.controller.Controller;
import com.example.demo.views.LevelView;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevelParentTest {

    private LevelParent testLevel;
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

        // Create a concrete subclass for testing LevelParent
        testLevel = new TestLevelParent(600, 800, mockController); // Ensure width and height match expectations
    }

    @Test
    void testSceneInitialization() {
        Scene scene = testLevel.initializeScene();
        assertNotNull(scene, "Scene should be initialized");
        assertEquals(800, scene.getWidth(), "Scene width should match the expected value");
        assertEquals(600, scene.getHeight(), "Scene height should match the expected value");
    }

    @Test
    void testUserInitialization() {
        UserPlane user = testLevel.getUser();
        assertNotNull(user, "UserPlane should be initialized");
        assertEquals(5, user.getHealth(), "Initial user health should be 5");
    }

    @Test
    void testAddEnemyUnit() {
        // Create a real UserPlane (or a proper subclass)
        UserPlane enemy = new UserPlane(5, testLevel.getRoot().getScene()); // Example constructor
        int initialEnemies = testLevel.getCurrentNumberOfEnemies();

        // Add the enemy to the level
        testLevel.addEnemyUnit(enemy);

        // Assert the enemy count has incremented
        assertEquals(initialEnemies + 1, testLevel.getCurrentNumberOfEnemies(), "Enemy count should increment by 1");

        // Assert the enemy has been added to the scene graph
        assertTrue(testLevel.getRoot().getChildren().contains(enemy), "Enemy should be added to the root");
    }



    @Test
    void testLoseGame() {
        testLevel.getUser().destroy();
        assertTrue(testLevel.userIsDestroyed(), "User should be destroyed");
        testLevel.loseGame();
        // Verifications for game-over logic can be added here
    }
}

// Concrete subclass of LevelParent for testing purposes
class TestLevelParent extends LevelParent {

    public TestLevelParent(double screenHeight, double screenWidth, Controller controller) {
        // Use an empty string for the background image name
        super("", screenHeight, screenWidth, 5, controller);
    }

    @Override
    protected void initializeFriendlyUnits() {
        // Minimal implementation for testing
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        // No-op for testing
    }

    @Override
    protected void spawnEnemyUnits() {
        // No-op for testing
    }

    @Override
    protected LevelView instantiateLevelView() {
        return Mockito.mock(LevelView.class);
    }
}
