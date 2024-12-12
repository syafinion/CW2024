package com.example.demo.views;

import com.example.demo.actors.Boss;
import com.example.demo.actors.UserPlane;
import com.example.demo.controller.Controller;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevelUIManagerTest {

    private LevelUIManager levelUIManager;
    private Group root;
    private Controller mockController;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Initialize the root Group
        root = new Group();

        // Mock the Controller
        mockController = Mockito.mock(Controller.class);

        // Initialize LevelUIManager
        levelUIManager = new LevelUIManager(800, 600, root, mockController);
    }

    @Test
    void testUpdateLevelView() {
        // Mock UserPlane and Boss
        UserPlane userPlane = Mockito.mock(UserPlane.class);
        Mockito.when(userPlane.getHealth()).thenReturn(3);

        Boss boss = Mockito.mock(Boss.class);
        Mockito.when(boss.getHealth()).thenReturn(75);
        Mockito.when(boss.isShielded()).thenReturn(true);
        Mockito.when(boss.getTranslateX()).thenReturn(200.0);
        Mockito.when(boss.getTranslateY()).thenReturn(100.0);

        // Mock LevelViewLevelTwo
        LevelViewLevelTwo levelView = Mockito.mock(LevelViewLevelTwo.class);

        // Call updateLevelView
        levelUIManager.updateLevelView(userPlane, levelView, boss);

        // Verify the heart display is updated
        Mockito.verify(levelView).removeHearts(3);

        // Verify boss health bar and shield updates
        Mockito.verify(levelView).updateBossHealthBar(75, 100);
        Mockito.verify(levelView).updateShieldPosition(200.0, 100.0);
        Mockito.verify(levelView).showShield();
    }

    @Test
    void testShowGameOverMenu() {
        // Call showGameOverMenu
        levelUIManager.showGameOverMenu(() -> System.out.println("Restart Level"), () -> System.out.println("Go to Main Menu"));

        // Verify the game over menu is added to the root
        assertFalse(root.getChildren().isEmpty(), "Root group should not be empty after adding game over menu");
    }

    @Test
    void testShowWinMenu() {
        // Call showWinMenu
        levelUIManager.showWinMenu(() -> System.out.println("Restart to Level One"), () -> System.out.println("Go to Main Menu"));

        // Verify the win menu is added to the root
        assertFalse(root.getChildren().isEmpty(), "Root group should not be empty after adding win menu");
    }

    @Test
    void testStartCountdown() throws Exception {
        final Object lock = new Object();
        boolean[] onCompleteCalled = {false};

        Platform.runLater(() -> {
            levelUIManager.startCountdown(() -> {
                synchronized (lock) {
                    onCompleteCalled[0] = true;
                    lock.notify();
                }
            }, "2");

            // Verify countdown text is added to root
            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Text), "Countdown text should be added to the root");
        });

        synchronized (lock) {
            lock.wait(5000); // Wait for countdown to complete
        }

        assertTrue(onCompleteCalled[0], "onComplete should be called after countdown");
    }
}
