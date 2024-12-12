package com.example.demo.levels;

import com.example.demo.controller.Controller;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevelOneTest {

    private LevelOne levelOne;
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

        // Initialize LevelOne
        levelOne = new LevelOne(800, 600, mockController);
    }

    @Test
    void testBackgroundImageName() {
        assertEquals(
                "/com/example/demo/images/levelonebg.gif",
                LevelOne.BACKGROUND_IMAGE_NAME,
                "Background image name should match the expected value"
        );
    }

    @Test
    void testPlayerInitialHealth() {
        assertNotNull(levelOne.getUser(), "UserPlane should be initialized");
        assertEquals(5, levelOne.getUser().getHealth(), "Initial player health should be 5");
    }

    @Test
    void testKillTargetNotReachedInitially() {
        assertFalse(levelOne.userHasReachedKillTarget(), "Kill target should not be reached initially");
    }

    @Test
    void testKillTargetAdvancement() {
        for (int i = 0; i < 9; i++) {
            levelOne.getUser().incrementKillCount();
        }
        assertFalse(levelOne.userHasReachedKillTarget(), "Kill target should not be reached with 9 kills");

        levelOne.getUser().incrementKillCount();
        assertTrue(levelOne.userHasReachedKillTarget(), "Kill target should be reached with 10 kills");
    }

    @Test
    void testEnemySpawnProbability() {
        assertEquals(
                0.20,
                LevelOne.ENEMY_SPAWN_PROBABILITY,
                "Enemy spawn probability should be 0.20"
        );
    }

    @Test
    void testGameOverWhenPlayerDestroyed() {
        levelOne.getUser().destroy();
        assertTrue(levelOne.getUser().isDestroyed(), "Player should be destroyed when health is zero");
    }
}
