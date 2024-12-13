package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Represents the "You Win" image displayed at the end of the game.
 * <p>
 * This class handles the display of the win image, including scaling
 * and positioning it relative to the screen dimensions.
 * </p>
 */
public class WinImage extends ImageView {
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	/**
	 * Constructs a {@code WinImage} and positions it based on screen dimensions.
	 *
	 * @param screenWidth the width of the screen
	 * @param screenHeight the height of the screen
	 */
	public WinImage(double screenWidth, double screenHeight) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

		// Scale the image to make it smaller
		setFitWidth(400); // Adjust this value as needed
		setPreserveRatio(true);

		// Center the image horizontally and position it above the menu
		setLayoutX((screenWidth - getFitWidth()) / 2);
		setLayoutY(screenHeight / 2 - getFitHeight() - 450); // Adjust vertical position (closer to the menu)
	}

	/**
	 * Displays the win image by making it visible.
	 */
	public void showWinImage() {
		setVisible(true);
	}
}
