package com.example.demo.levels;

import com.example.demo.controller.Controller;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevelTwoTest {

    private LevelTwo levelTwo;
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

        // Initialize LevelTwo
        levelTwo = new LevelTwo(800, 600, mockController);
    }

    @Test
    void testBackgroundImageName() {
        assertEquals(
                "/com/example/demo/images/leveltwobg.gif",
                LevelTwo.BACKGROUND_IMAGE_NAME,
                "Background image name should match the expected value"
        );
    }

    @Test
    void testPlayerInitialHealth() {
        assertNotNull(levelTwo.getUser(), "UserPlane should be initialized");
        assertEquals(5, levelTwo.getUser().getHealth(), "Initial player health should be 5");
    }

    @Test
    void testKillTargetNotReachedInitially() {
        assertFalse(levelTwo.getUser().getNumberOfKills() >= 20, "Kill target should not be reached initially");
    }

    @Test
    void testKillTargetAdvancement() {
        for (int i = 0; i < 19; i++) {
            levelTwo.getUser().incrementKillCount();
        }
        assertFalse(levelTwo.getUser().getNumberOfKills() >= 20, "Kill target should not be reached with 19 kills");

        levelTwo.getUser().incrementKillCount();
        assertTrue(levelTwo.getUser().getNumberOfKills() >= 20, "Kill target should be reached with 20 kills");
    }

    @Test
    void testEnemySpawnProbability() {
        assertEquals(
                0.30,
                LevelTwo.ENEMY_SPAWN_PROBABILITY,
                "Enemy spawn probability should be 0.30"
        );
    }

    @Test
    void testGameOverWhenPlayerDestroyed() {
        levelTwo.getUser().destroy();
        assertTrue(levelTwo.getUser().isDestroyed(), "Player should be destroyed when health is zero");
    }

    @Test
    void testLevelTransition() {
        for (int i = 0; i < 20; i++) {
            levelTwo.getUser().incrementKillCount();
        }
        assertTrue(levelTwo.getUser().getNumberOfKills() >= 20, "Kill target should be reached with 20 kills");
        // Simulate level transition
        assertDoesNotThrow(() -> levelTwo.goToNextLevel("com.example.demo.levels.LevelThree"));
    }
}
