package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class ShieldImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";

	public static final int SHIELD_SIZE = 200;

	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		try {
			this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
			this.setFitHeight(SHIELD_SIZE);
			this.setFitWidth(SHIELD_SIZE);
			this.setVisible(true);
		} catch (NullPointerException e) {
			System.err.println("Image file not found: " + IMAGE_NAME);
		} catch (Exception e) {
			System.err.println("Error loading shield image: " + e.getMessage());
		}
	}



	public void showShield() {
		System.out.println("Showing shield");
		this.setVisible(true);
		this.toFront();
	}
	
	public void hideShield() {
		System.out.println("Hiding shield");
		this.setVisible(false);
		this.toFront();
	}

}
