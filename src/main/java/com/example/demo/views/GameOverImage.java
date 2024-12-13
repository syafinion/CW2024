package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Displays the "Game Over" image when the player loses the game.
 * <p>
 * This class extends {@link ImageView} and provides logic to scale and position
 * the image in the game window.
 * </p>
 */
public class GameOverImage extends ImageView {

	/** Path to the "Game Over" image file. */
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.gif";

	/**
	 * Constructs a new {@code GameOverImage} and positions it on the screen.
	 *
	 * @param screenWidth the width of the game screen
	 * @param screenHeight the height of the game screen
	 */
	public GameOverImage(double screenWidth, double screenHeight) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Scale the image to make it smaller
		setFitWidth(400); // Adjust this value as needed
		setPreserveRatio(true);

		// Center the image horizontally and position it above the menu
		setLayoutX((screenWidth - getFitWidth()) / 2);
		setLayoutY(screenHeight / 2 - getFitHeight() - 450); // Adjust vertical position (above the menu)
	}
}
