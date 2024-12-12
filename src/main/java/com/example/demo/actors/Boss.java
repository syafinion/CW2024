package com.example.demo.actors;

import com.example.demo.levels.LevelParent;
import com.example.demo.levels.LevelThree;
import com.example.demo.views.LevelViewLevelTwo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .0083;
	private static final double BOSS_SHIELD_PROBABILITY = 0.2; // test
	private static final int SHIELD_COOLDOWN_FRAMES = 300;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private int shieldCooldownFrames;
	private static final int SHIELD_MAX_HEALTH = 50; // Max health for the shield
	private int shieldHealth; // Current shield health
	private boolean shieldDestroyed = false; // Tracks whether the shield is permanently destroyed

	private LevelParent level;
	public Boss(LevelParent level){
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		this.level = level;
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();

		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}

		// Dynamically track boss position for the shield
		System.out.println("Boss position: (" + getTranslateX() + ", " + getTranslateY() + ")");
	}


	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		notifyShieldPosition(); // Notify the LevelView about the updated shield position
		System.out.println("Shield is " + (isShielded ? "active" : "inactive"));
	}

	private void notifyShieldPosition() {
		if (level instanceof LevelThree) {
			double bossActualX = getLayoutX() + getTranslateX();
			double bossActualY = getLayoutY() + getTranslateY();
			((LevelThree) level).updateBossShieldPosition(bossActualX, bossActualY);
		}
	}


	@Override
	public ActiveActorDestructible fireProjectile() {
		if (bossFiresInCurrentFrame()) {
			// Calculate the fireball's position relative to the boss's current position
			double adjustedProjectileX = getLayoutX() + getTranslateX() - 50; // In front of the boss
			double adjustedProjectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
			return new BossProjectile(adjustedProjectileX, adjustedProjectileY, level.getUserPlane());
		}
		return null;
	}


	private void updateShieldHealthBar() {
		if (level.getLevelView() instanceof LevelViewLevelTwo) {
			LevelViewLevelTwo levelViewLevelTwo = (LevelViewLevelTwo) level.getLevelView();
			levelViewLevelTwo.updateShieldHealthBar(shieldHealth, SHIELD_MAX_HEALTH);
		}
	}

	@Override
	public void takeDamage() {
		if (isShielded) {
			shieldHealth--;
			updateShieldHealthBar(); // Update the shield health bar
			showShieldHitEffect();   // Apply the hit effect
			if (shieldHealth <= 0) {
				deactivateShield(); // Deactivate the shield
				shieldDestroyed = true; // Permanently destroy the shield
				System.out.println("Shield is permanently destroyed.");
			}
		} else {
			super.takeDamage(); // Regular damage when not shielded
		}
	}


	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	private void updateShield() {
		if (shieldDestroyed) {
			isShielded = false; // Ensure shield remains inactive
			System.out.println("Shield is permanently destroyed and cannot be reactivated.");
			return;
		}

		if (isShielded) {
			framesWithShieldActivated++;
			System.out.println("Shield active. Frame count: " + framesWithShieldActivated);
			if (shieldExhausted()) {
				deactivateShield();
			}
		} else {
			// Reduce cooldown timer if shield is not active
			if (shieldCooldownFrames > 0) {
				shieldCooldownFrames--;
			} else if (shieldShouldBeActivated()) {
				activateShield();
			}
		}
		System.out.println("Shield is " + (isShielded ? "active" : "inactive") + ", Cooldown: " + shieldCooldownFrames);
	}



	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}


	private boolean shieldShouldBeActivated() {
		// Activate the shield when:
		// 1. The boss's health is below 75%, AND
		// 2. A random chance based on the configured BOSS_SHIELD_PROBABILITY, AND
		// 3. No cooldown is in effect.

		if (shieldDestroyed) {
			return false;
		}

		boolean shouldActivate = getHealth() <= 75 && Math.random() < BOSS_SHIELD_PROBABILITY && shieldCooldownFrames == 0;
		System.out.println("Shield activation condition met: " + shouldActivate);
		return shouldActivate;
	}

	private boolean shieldExhausted() {
		// Deactivate the shield after it has been active for a fixed duration of 400 frames.
		return framesWithShieldActivated >= 400; // You can adjust this value as needed
	}

	private void activateShield() {
		if (!isShielded) {
			isShielded = true;
			if (shieldHealth <= 0) { // Only set shield health if it hasn't been initialized or was fully depleted
				shieldHealth = SHIELD_MAX_HEALTH;
			}
			framesWithShieldActivated = 0; // Reset the activation frame count
			System.out.println("Shield activated with health: " + shieldHealth);
		}
	}

	private void deactivateShield() {
		isShielded = false;
		shieldCooldownFrames = SHIELD_COOLDOWN_FRAMES; // Start cooldown period
		System.out.println("Shield deactivated! Cooldown started.");
	}

	private void showShieldHitEffect() {
		if (!isShielded) {
			return; // Only apply the effect if the shield is active
		}

		Timeline flashEffect = new Timeline(
				new KeyFrame(Duration.seconds(0), e -> {
					this.setStyle("-fx-opacity: 0.5; -fx-effect: dropshadow(gaussian, cyan, 30, 0.8, 0, 0);");
				}),
				new KeyFrame(Duration.seconds(0.1), e -> {
					this.setStyle("-fx-opacity: 1.0; -fx-effect: none;");
				})
		);
		flashEffect.setCycleCount(1);
		flashEffect.play();
	}


	public int getHealth() {
		return super.getHealth(); // or just return getHealth();
	}


	public boolean isShielded() {
		return isShielded;
	}


}
