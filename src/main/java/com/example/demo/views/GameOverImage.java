package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

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
