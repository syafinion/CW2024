package com.example.demo.actors;

import javafx.scene.Scene;
import javafx.scene.media.AudioClip;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 12;
	private static final int HORIZONTAL_VELOCITY = 12; // New constant for horizontal velocity
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;

	private final Scene scene;

	private static final String SHOOTING_SOUND = "/com/example/demo/images/shootingsound.wav";
	private final AudioClip shootingSound;


	private int horizontalVelocityMultiplier; // New variable for horizontal movement
	private static final int FIRE_COOLDOWN_MILLIS = 300;
	private double gunshotVolume = 0.5; // Default volume (50%)

	private long lastFireTime;

	public UserPlane(int initialHealth, Scene scene) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.scene = scene; // Save the scene for dynamic height and width calculation
		velocityMultiplier = 0;
		horizontalVelocityMultiplier = 0; // Initialize horizontal movement
		this.lastFireTime = 0;

		// Initialize the shooting sound
		shootingSound = new AudioClip(getClass().getResource(SHOOTING_SOUND).toExternalForm());
	}

	public void setGunshotVolume(double volume) {
		this.gunshotVolume = Math.min(1.0, Math.max(0.0, volume)); // Clamp between 0.0 and 1.0
		shootingSound.setVolume(this.gunshotVolume); // Update the AudioClip volume

		// Debugging
		if (this.gunshotVolume == 0) {
			System.out.println("Gunshot volume muted.");
		} else {
			System.out.println("Gunshot volume set to: " + (this.gunshotVolume * 100) + "%");
		}
	}


	public void setHealth(int health) {
		this.health = health;
		System.out.println("UserPlane health set to: " + this.health);
	}

	@Override
	public void updatePosition() {
		double screenWidth = scene.getWidth();
		double screenHeight = scene.getHeight();

		// Define precise bounds
		double upperBound = 0; // Top of the screen
		double lowerBound = screenHeight; // Bottom of the screen
		double leftBound = 0; // Left of the screen
		double rightBound = screenWidth; // Right of the screen

		// Calculate new positions
		double newTranslateY = getTranslateY() + VERTICAL_VELOCITY * velocityMultiplier;
		double newTranslateX = getTranslateX() + HORIZONTAL_VELOCITY * horizontalVelocityMultiplier;

		// Restrict movement within vertical bounds
		double jetTopPosition = getLayoutY() + newTranslateY;
		double jetBottomPosition = jetTopPosition + getBoundsInParent().getHeight();
		if (jetTopPosition < upperBound) {
			newTranslateY = upperBound - getLayoutY();
		} else if (jetBottomPosition > lowerBound) {
			newTranslateY = lowerBound - getBoundsInParent().getHeight() - getLayoutY();
		}

		// Restrict movement within horizontal bounds
		double jetLeftPosition = getLayoutX() + newTranslateX;
		double jetRightPosition = jetLeftPosition + getBoundsInParent().getWidth();
		if (jetLeftPosition < leftBound) {
			newTranslateX = leftBound - getLayoutX();
		} else if (jetRightPosition > rightBound) {
			newTranslateX = rightBound - getBoundsInParent().getWidth() - getLayoutX();
		}

		// Apply calculated positions
		setTranslateY(newTranslateY);
		setTranslateX(newTranslateX);
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastFireTime < FIRE_COOLDOWN_MILLIS) {
			// Cooldown not elapsed, prevent firing
			return null;
		}
		lastFireTime = currentTime; // Update last fire time

		// Ensure the shooting sound volume is set to the latest value
		shootingSound.setVolume(gunshotVolume);
		if (gunshotVolume > 0) {
			shootingSound.play(); // Only play the sound if volume > 0
		}

		// Calculate the projectile's position relative to the jet's current position
		double adjustedProjectileX = getLayoutX() + getTranslateX() + PROJECTILE_X_POSITION;
		double adjustedProjectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
		return new UserProjectile(adjustedProjectileX, adjustedProjectileY);
	}


	private boolean isMoving() {
		return velocityMultiplier != 0 || horizontalVelocityMultiplier != 0;
	}

	public void moveUp() {
		velocityMultiplier = -1;
	}

	public void moveDown() {
		velocityMultiplier = 1;
	}

	// New methods for horizontal movement
	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	public void stop() {
		velocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

}
