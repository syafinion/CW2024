package com.example.demo.actors;

/**
 * Represents a projectile fired by an enemy plane.
 * <p>
 * The projectile initially homes in on the player's plane within a specified
 * distance and then transitions to a straight-line trajectory. It is destroyed
 * if it exceeds a set lifespan or moves off-screen.
 * </p>
 */
public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "enemyFire.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final double SPEED = 7.0; // Projectile speed
	private static final int MAX_LIFESPAN = 300; // Number of frames before disappearing
	private static final double HOMING_DISTANCE = 300; // Max distance for homing behavior

	private final UserPlane userPlane; // Reference to the player's plane
	private int lifespan; // Tracks how long the projectile has existed
	private boolean isHoming; // Whether the projectile is still homing
	private double angle; // Angle of movement for the straight-line trajectory

	/**
	 * Constructs an EnemyProjectile with the specified position and target player plane.
	 *
	 * @param initialXPos the initial X-coordinate of the projectile
	 * @param initialYPos the initial Y-coordinate of the projectile
	 * @param userPlane   the player's plane that the projectile targets
	 */
	public EnemyProjectile(double initialXPos, double initialYPos, UserPlane userPlane) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		this.userPlane = userPlane;
		this.lifespan = 0;
		this.isHoming = true;
		this.angle = 0; // Default angle, to be calculated when homing stops
	}

	@Override
	public void updatePosition() {
		lifespan++;

		if (isHoming) {
			// Calculate the direction vector towards the user's position
			double deltaX = userPlane.getTranslateX() + userPlane.getLayoutX() - (this.getTranslateX() + this.getLayoutX());
			double deltaY = userPlane.getTranslateY() + userPlane.getLayoutY() - (this.getTranslateY() + this.getLayoutY());
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			if (distance <= HOMING_DISTANCE && distance > IMAGE_HEIGHT / 2) { // Add a minimum distance check
				// Normalize the direction vector and scale it by the projectile speed
				double normalizedX = (deltaX / distance) * SPEED;
				double normalizedY = (deltaY / distance) * SPEED;

				this.setTranslateX(this.getTranslateX() + normalizedX);
				this.setTranslateY(this.getTranslateY() + normalizedY);
			} else {
				// Set angle and stop homing if the target is out of range or too close
				calculateAngleForStraightLine(deltaX, deltaY);
				isHoming = false;
			}
		} else {
			// Move in a straight line after homing is deactivated
			this.setTranslateX(this.getTranslateX() + SPEED * Math.cos(angle));
			this.setTranslateY(this.getTranslateY() + SPEED * Math.sin(angle));
		}

		// Destroy projectile if it exceeds its lifespan or is off-screen
		if (lifespan > MAX_LIFESPAN || isOffScreen()) {
			this.destroy();
		}
	}


	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		// Prevent shooting if the user plane has passed the enemy plane
		if (userPlane.getTranslateY() + userPlane.getLayoutY() < this.getTranslateY() + this.getLayoutY()) {
			throw new UnsupportedOperationException("EnemyProjectile cannot fire projectiles because the user plane has passed.");
		}

		// Code for firing projectiles (if allowed in other cases)
		return new EnemyProjectile(this.getTranslateX(), this.getTranslateY(), userPlane);
	}

	/**
	 * Calculates the angle for the projectile's straight-line movement.
	 *
	 * @param deltaX the X-direction displacement
	 * @param deltaY the Y-direction displacement
	 */
	private void calculateAngleForStraightLine(double deltaX, double deltaY) {
		this.angle = Math.atan2(deltaY, deltaX); // Calculate angle for straight-line movement
	}

	/**
	 * Checks if the projectile has moved off the screen.
	 *
	 * @return true if the projectile is off-screen, false otherwise
	 */
	private boolean isOffScreen() {
		// Check if the projectile has moved off the screen
		double x = this.getTranslateX() + this.getLayoutX();
		double y = this.getTranslateY() + this.getLayoutY();
		return x < -IMAGE_HEIGHT || x > 1920 + IMAGE_HEIGHT // Assuming screen width
				|| y < -IMAGE_HEIGHT || y > 1080 + IMAGE_HEIGHT; // Assuming screen height
	}
}
