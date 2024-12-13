package com.example.demo.actors;

import javafx.scene.image.*;

/**
 * Represents an active actor in the game, displayed as an image.
 * <p>
 * This class provides base functionality for positioning and movement of
 * actors in the game, such as horizontal and vertical movement.
 * </p>
 */

public abstract class ActiveActor extends ImageView {
	
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	/**
	 * Constructs an {@code ActiveActor} with the specified image, size, and initial position.
	 *
	 * @param imageName   the name of the image file
	 * @param imageHeight the height of the image
	 * @param initialXPos the initial X-coordinate of the actor
	 * @param initialYPos the initial Y-coordinate of the actor
	 */

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		//this.setImage(new Image(IMAGE_LOCATION + imageName));
		this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}
	/**
	 * Updates the position of the actor. This method must be implemented by subclasses
	 * to define specific movement behavior.
	 */
	public abstract void updatePosition();
	/**
	 * Moves the actor horizontally by the specified amount.
	 *
	 * @param horizontalMove the amount to move horizontally
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}
	/**
	 * Moves the actor vertically by the specified amount.
	 *
	 * @param verticalMove the amount to move vertically
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}

}
