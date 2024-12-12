package com.example.demo.actors;

import javafx.scene.Scene;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
//	private static final double Y_UPPER_BOUND = -40;
//	private static final double Y_LOWER_BOUND = 600.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;

	private final Scene scene;

	private static final int HORIZONTAL_VELOCITY = 8; // New constant for horizontal velocity
	private int horizontalVelocityMultiplier; // New variable for horizontal movement

	public UserPlane(int initialHealth, Scene scene) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.scene = scene; // Save the scene for dynamic height and width calculation
		velocityMultiplier = 0;
		horizontalVelocityMultiplier = 0; // Initialize horizontal movement
	}
	public void setHealth(int health) {
		this.health = health;
		System.out.println("UserPlane health set to: " + this.health);
	}


	@Override
	public void updatePosition() {
		if (isMoving()) {
			double screenWidth = scene.getWidth();
			double screenHeight = scene.getHeight();

			// Define precise bounds
			double upperBound = 0; // Top of the screen
			double lowerBound = screenHeight; // Bottom of the screen
			double leftBound = 0; // Left of the screen
			double rightBound = screenWidth; // Right of the screen

			// Move vertically
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);

			// Move horizontally
			this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);

			// Restrict movement within vertical bounds
			double newTranslateY = getTranslateY();
			double jetTopPosition = getLayoutY() + newTranslateY;
			double jetBottomPosition = jetTopPosition + getBoundsInParent().getHeight();

			if (jetTopPosition < upperBound) {
				this.setTranslateY(upperBound - getLayoutY());
			} else if (jetBottomPosition > lowerBound) {
				this.setTranslateY(lowerBound - getBoundsInParent().getHeight() - getLayoutY());
			}

			// Restrict movement within horizontal bounds
			double newTranslateX = getTranslateX();
			double jetLeftPosition = getLayoutX() + newTranslateX;
			double jetRightPosition = jetLeftPosition + getBoundsInParent().getWidth();

			if (jetLeftPosition < leftBound) {
				this.setTranslateX(leftBound - getLayoutX());
			} else if (jetRightPosition > rightBound) {
				this.setTranslateX(rightBound - getBoundsInParent().getWidth() - getLayoutX());
			}
		}
	}


	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
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
