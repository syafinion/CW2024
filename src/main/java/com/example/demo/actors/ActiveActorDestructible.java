package com.example.demo.actors;

import com.example.demo.utilities.Destructible;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Represents a destructible active actor in the game.
 * <p>
 * This class extends {@link ActiveActor} and provides additional functionality
 * for handling destruction, collisions, and damage.
 * </p>
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;

	/**
	 * Constructs a {@code ActiveActorDestructible} with the specified parameters.
	 *
	 * @param imageName   the name of the image file
	 * @param imageHeight the height of the image
	 * @param initialXPos the initial X-coordinate of the actor
	 * @param initialYPos the initial Y-coordinate of the actor
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	/**
	 * Updates the position of the actor. This method must be implemented by subclasses
	 * to define specific movement behavior.
	 */
	@Override
	public abstract void updatePosition();
	/**
	 * Updates the state of the actor.
	 * <p>
	 * This method must be implemented by subclasses to define behavior such as animations
	 * or interactions during each frame of the game.
	 * </p>
	 */
	public abstract void updateActor();
	/**
	 * Fires a projectile from the actor.
	 * <p>
	 * This method must be implemented by subclasses to define projectile behavior.
	 * </p>
	 *
	 * @return a new {@link ActiveActorDestructible} representing the projectile, or {@code null} if no projectile is fired
	 */
	public abstract ActiveActorDestructible fireProjectile();
	/**
	 * Takes damage and handles any effects related to being damaged.
	 * This method must be implemented by subclasses.
	 */
	@Override
	public abstract void takeDamage();
	/**
	 * Marks the actor as destroyed and updates its state.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}
	/**
	 * Sets the destroyed state of the actor.
	 *
	 * @param isDestroyed {@code true} if the actor is destroyed, {@code false} otherwise
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	/**
	 * Checks whether the actor is destroyed.
	 *
	 * @return {@code true} if the actor is destroyed, {@code false} otherwise
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
	/**
	 * Gets the adjusted bounding box of the actor for collision detection.
	 * <p>
	 * Shrinks the original bounds to make collision detection more precise.
	 * </p>
	 *
	 * @return the adjusted {@link Bounds} of the actor
	 */
	public Bounds getAdjustedBounds() {
		Bounds originalBounds = this.getBoundsInParent();
		double widthShrinkFactor = 0.8; // Shrink width to 80% of the original
		double heightShrinkFactor = 0.4; // Shrink height to 60% of the original

		double width = originalBounds.getWidth() * widthShrinkFactor;
		double height = originalBounds.getHeight() * heightShrinkFactor;
		double x = originalBounds.getMinX() + (originalBounds.getWidth() - width) / 2;
		double y = originalBounds.getMinY() + (originalBounds.getHeight() - height) / 2;

		return new BoundingBox(x, y, width, height);
	}


	/**
	 * Creates a visual representation of the bounding box for debugging purposes.
	 *
	 * @return a {@link Rectangle} representing the bounding box
	 */
	// TESTING
	// Temporary method to visualize the bounding box
	public Rectangle getBoundingBoxHighlight() {
		Bounds bounds = getAdjustedBounds();
		Rectangle highlight = new Rectangle(bounds.getWidth(), bounds.getHeight());
		highlight.setX(bounds.getMinX());
		highlight.setY(bounds.getMinY());
		highlight.setStroke(Color.RED);
		highlight.setFill(Color.TRANSPARENT);
		return highlight;
	}

	/**
	 * Updates the position of the visual bounding box for debugging purposes.
	 *
	 * @param highlight the bounding box rectangle to update
	 */
	// Temporary method to update the bounding box highlight
	public void updateBoundingBoxHighlight(Rectangle highlight) {
		Bounds bounds = getAdjustedBounds();
		highlight.setX(bounds.getMinX());
		highlight.setY(bounds.getMinY());
		highlight.setWidth(bounds.getWidth());
		highlight.setHeight(bounds.getHeight());
	}

	/**
	 * Checks if the actor is visible on the screen.
	 *
	 * @param screenWidth  the width of the screen
	 * @param screenHeight the height of the screen
	 * @return {@code true} if the actor is visible, {@code false} otherwise
	 */
	public boolean isVisibleOnScreen(double screenWidth, double screenHeight) {
		Bounds bounds = getBoundsInParent();
		return bounds.getMaxX() > 0 && bounds.getMinX() < screenWidth &&
				bounds.getMaxY() > 0 && bounds.getMinY() < screenHeight;
	}


}
