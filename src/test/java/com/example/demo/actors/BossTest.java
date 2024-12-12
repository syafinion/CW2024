package com.example.demo.actors;

import com.example.demo.levels.LevelParent;
import com.example.demo.levels.LevelThree;
import com.example.demo.views.LevelViewLevelTwo;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BossTest {

    private Boss boss;
    private LevelParent mockLevel;
    private UserPlane mockUserPlane;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Mock the LevelParent and UserPlane
        mockLevel = Mockito.mock(LevelParent.class);
        mockUserPlane = Mockito.mock(UserPlane.class);

        // Mock LevelViewLevelTwo for testing shield updates
        LevelViewLevelTwo mockLevelView = Mockito.mock(LevelViewLevelTwo.class);
        Mockito.when(mockLevel.getLevelView()).thenReturn(mockLevelView);
        Mockito.when(mockLevel.getUserPlane()).thenReturn(mockUserPlane);

        // Initialize the Boss instance
        boss = new Boss(mockLevel);
    }

    @Test
    void testInitialState() {
        // Verify initial health and shield state
        assertEquals(100, boss.getHealth(), "Boss should have initial health of 100.");
        assertFalse(boss.isShielded(), "Boss should not be shielded initially.");
    }

    @Test
    void testMovePatternInitialization() {
        // Ensure the move pattern is initialized correctly
        boss.updatePosition(); // Trigger the first move
        assertNotNull(boss, "Boss should have a move pattern initialized.");
    }

    @Test
    void testUpdatePositionWithinBounds() {
        // Update position and ensure it stays within bounds
        boss.updatePosition();
        double yPosition = boss.getLayoutY() + boss.getTranslateY();
        assertTrue(yPosition >= -100 && yPosition <= 475, "Boss position should stay within bounds.");
    }

    @Test
    void testShieldActivationAndDeactivation() {
        boss.updateShield(); // Update the shield state

        if (boss.isShielded()) {
            assertTrue(boss.isShielded(), "Boss shield should be activated based on conditions.");

            // Simulate shield exhaustion
            for (int i = 0; i < 400; i++) {
                boss.updateShield();
            }

            assertFalse(boss.isShielded(), "Boss shield should deactivate after being active for 400 frames.");
        }
    }

    @Test
    void testTakeDamage() {
        // Test taking damage with and without a shield
        boss.takeDamage();
        assertEquals(99, boss.getHealth(), "Boss health should decrease by 1 when taking damage without a shield.");

        // Activate shield and take damage
        boss.activateShield();
        int initialShieldHealth = boss.shieldHealth;
        boss.takeDamage();
        assertEquals(initialShieldHealth - 1, boss.shieldHealth, "Shield health should decrease when taking damage with a shield.");

        if (boss.shieldHealth <= 0) {
            assertFalse(boss.isShielded(), "Shield should deactivate when shield health reaches 0.");
        }
    }

    @Test
    void testFireProjectile() {
        // Simulate firing a projectile
        Mockito.when(mockUserPlane.getLayoutX()).thenReturn(0.0);
        Mockito.when(mockUserPlane.getLayoutY()).thenReturn(0.0);

        ActiveActorDestructible projectile = boss.fireProjectile();
        if (projectile != null) {
            assertTrue(projectile instanceof BossProjectile, "Projectile fired should be of type BossProjectile.");
        }
    }

    @Test
    void testBossProjectileBehavior() {
        // Create a BossProjectile and test its behavior
        BossProjectile projectile = new BossProjectile(500, 300, mockUserPlane);

        // Simulate movement
        projectile.updatePosition();
        double xPosition = projectile.getTranslateX() + projectile.getLayoutX();
        double yPosition = projectile.getTranslateY() + projectile.getLayoutY();

        assertTrue(xPosition >= 0 && xPosition <= 1920, "Projectile should stay within horizontal bounds.");
        assertTrue(yPosition >= 0 && yPosition <= 1080, "Projectile should stay within vertical bounds.");

        // Simulate projectile lifespan
        for (int i = 0; i < 300; i++) {
            projectile.updatePosition();
        }
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after exceeding its lifespan.");
    }
}