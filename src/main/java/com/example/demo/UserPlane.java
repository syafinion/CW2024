package com.example.demo;

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

	public UserPlane(int initialHealth, Scene scene) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.scene = scene; // Save the scene for dynamic height calculation
		velocityMultiplier = 0;
	}

	@Override
	public void updatePosition() {
		if (isMoving()) {
			double screenHeight = scene.getHeight();

			// Define precise bounds
			double upperBound = 0; // Top of the screen
			double lowerBound = screenHeight; // Bottom of the screen

			// Move vertically
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);

			// Calculate the jet's new position
			double newTranslateY = getTranslateY();
			double jetTopPosition = getLayoutY() + newTranslateY;
			double jetBottomPosition = jetTopPosition + getBoundsInParent().getHeight();

			// Restrict movement within bounds
			if (jetTopPosition < upperBound) {
				this.setTranslateY(upperBound - getLayoutY()); // Align precisely to the top
			} else if (jetBottomPosition > lowerBound) {
				this.setTranslateY(lowerBound - getBoundsInParent().getHeight() - getLayoutY()); // Align precisely to the bottom
			}
		}
	}














	@Override
	public void updateActor() {
		updatePosition();
	}
	
	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	public void moveUp() {
		velocityMultiplier = -1;
	}

	public void moveDown() {
		velocityMultiplier = 1;
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
