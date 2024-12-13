package com.example.demo.actors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Represents a fighter plane in the game.
 * <p>
 * Fighter planes are destructible actors that can take damage and fire projectiles.
 * </p>
 */

public abstract class FighterPlane extends ActiveActorDestructible {

    public int health;

	/**
	 * Constructs a FighterPlane with the specified image, position, and health.
	 *
	 * @param imageName   the name of the image representing the plane
	 * @param imageHeight the height of the image
	 * @param initialXPos the initial X-coordinate of the plane
	 * @param initialYPos the initial Y-coordinate of the plane
	 * @param health      the initial health of the plane
	 */
    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.health = health;
    }

	/**
	 * Fires a projectile from the fighter plane.
	 *
	 * @return the fired projectile
	 */
    public abstract ActiveActorDestructible fireProjectile();

    @Override
    public void takeDamage() {
        health--;
        showHitEffect(); // Add this to show the hit effect
        if (healthAtZero()) {
            this.destroy();
        }
    }

	/**
	 * Displays a hit effect when the plane takes damage.
	 */
    private void showHitEffect() {
        // Create a flash effect using a timeline
        Timeline flashEffect = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> this.setStyle("-fx-opacity: 0.5; -fx-effect: dropshadow(gaussian, white, 30, 0.8, 0, 0);")),
                new KeyFrame(Duration.seconds(0.1), e -> this.setStyle("-fx-opacity: 1.0; -fx-effect: none;"))
        );
        flashEffect.setCycleCount(1);
        flashEffect.play();
    }

	/**
	 * Gets the X-coordinate for a projectile to be fired from the plane.
	 *
	 * @param xPositionOffset the offset from the plane's X-coordinate
	 * @return the calculated X-coordinate
	 */
    protected double getProjectileXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

	/**
	 * Gets the Y-coordinate for a projectile to be fired from the plane.
	 *
	 * @param yPositionOffset the offset from the plane's Y-coordinate
	 * @return the calculated Y-coordinate
	 */

    protected double getProjectileYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

	/**
	 * Checks if the plane's health is zero.
	 *
	 * @return true if health is zero, false otherwise
	 */
    private boolean healthAtZero() {
        return health == 0;
    }

	/**
	 * Gets the current health of the plane.
	 *
	 * @return the current health
	 */
    public int getHealth() {
        return health;
    }


}
