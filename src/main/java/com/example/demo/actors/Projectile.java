package com.example.demo.actors;

/**
 * Represents a projectile in the game.
 * <p>
 * Projectiles are destructible actors that move across the screen and can take damage.
 * </p>
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a Projectile with the specified image and position.
	 *
	 * @param imageName   the name of the image representing the projectile
	 * @param imageHeight the height of the image
	 * @param initialXPos the initial X-coordinate of the projectile
	 * @param initialYPos the initial Y-coordinate of the projectile
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Updates the position of the projectile.
	 */
	@Override
	public abstract void updatePosition();

}
