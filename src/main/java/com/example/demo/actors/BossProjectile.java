package com.example.demo.actors;

public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final double SPEED = 15.0; // Projectile speed
	private static final double HOMING_DISTANCE = 500; // Max distance for homing behavior
	private static final int MAX_LIFESPAN = 300; // Number of frames before disappearing

	private final UserPlane userPlane; // Reference to the player's plane
	private boolean isHoming; // Whether the projectile is still homing
	private double angle; // Angle of movement for the straight-line trajectory
	private int lifespan;


	public BossProjectile(double initialXPos, double initialYPos, UserPlane userPlane) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		this.userPlane = userPlane;
		this.isHoming = true;
		this.angle = 0; // Default angle, to be calculated when homing stops
		this.lifespan = 0;
	}


	@Override
	public void updatePosition() {
		lifespan++;

		if (isHoming) {
			// Calculate the direction vector towards the player's position
			double deltaX = userPlane.getTranslateX() + userPlane.getLayoutX() - (this.getTranslateX() + this.getLayoutX());
			double deltaY = userPlane.getTranslateY() + userPlane.getLayoutY() - (this.getTranslateY() + this.getLayoutY());
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			// Stop homing if the projectile has passed the player
			if (deltaX < 0 || distance > HOMING_DISTANCE || distance < IMAGE_HEIGHT / 2) {
				// Calculate a straight-line angle based on the last direction
				calculateAngleForStraightLine(deltaX, deltaY);
				isHoming = false; // Stop homing
			} else {
				// Normalize the direction vector and scale it by the projectile speed
				double normalizedX = (deltaX / distance) * SPEED;
				double normalizedY = (deltaY / distance) * SPEED;

				this.setTranslateX(this.getTranslateX() + normalizedX);
				this.setTranslateY(this.getTranslateY() + normalizedY);
			}
		} else {
			// Move in a straight line after homing is deactivated
			this.setTranslateX(this.getTranslateX() + SPEED * Math.cos(angle));
			this.setTranslateY(this.getTranslateY() + SPEED * Math.sin(angle));
		}

		// Destroy the projectile if it exceeds its lifespan or moves off-screen
		if (lifespan > MAX_LIFESPAN || isOffScreen()) {
			this.destroy();
		}
	}

	private void calculateAngleForStraightLine(double deltaX, double deltaY) {
		this.angle = Math.atan2(deltaY, deltaX); // Calculate angle for straight-line movement
	}

	private boolean isOffScreen() {
		// Check if the projectile has moved off the screen
		double x = this.getTranslateX() + this.getLayoutX();
		double y = this.getTranslateY() + this.getLayoutY();
		return x < -IMAGE_HEIGHT || x > 1920 + IMAGE_HEIGHT // Assuming screen width
				|| y < -IMAGE_HEIGHT || y > 1080 + IMAGE_HEIGHT; // Assuming screen height
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		// BossProjectile does not fire further projectiles.
		throw new UnsupportedOperationException("BossProjectile cannot fire projectiles.");
	}
}
