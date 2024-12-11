package com.example.demo;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = .01;
	private final UserPlane userPlane;

	private boolean hasPassedPlayer; // Flag to indicate if the jet has passed the player

	public EnemyPlane(double initialXPos, double initialYPos, UserPlane userPlane) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
		this.userPlane = userPlane; // Initialize UserPlane reference
		this.hasPassedPlayer = false; // Initialize flag
	}

	@Override
	public void updatePosition() {
		// Move the jet horizontally
		moveHorizontally(HORIZONTAL_VELOCITY);

		// Check if the jet has passed the player
		if (!hasPassedPlayer && hasPassedPlayerPosition()) {
			hasPassedPlayer = true; // Set the flag to true
		}
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		// Allow shooting only if the jet has not passed the player
		if (!hasPassedPlayer && Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition, userPlane); // Pass UserPlane
		}
		return null;
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	private boolean hasPassedPlayerPosition() {
		// Check if the jet has moved past the player's X position
		return this.getTranslateX() + this.getLayoutX() < userPlane.getTranslateX() + userPlane.getLayoutX();
	}
}
