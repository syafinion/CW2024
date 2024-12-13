package com.example.demo.actors;

/**
 * Represents a projectile fired by the user's plane.
 * <p>
 * The UserProjectile moves horizontally across the screen and can take damage.
 * </p>
 */
public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 125;
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * Constructs a UserProjectile with the specified initial position.
	 *
	 * @param initialXPos the initial X-coordinate of the projectile
	 * @param initialYPos the initial Y-coordinate of the projectile
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		// UserProjectile does not fire further projectiles.
		throw new UnsupportedOperationException("UserProjectile cannot fire projectiles.");
	}
}
