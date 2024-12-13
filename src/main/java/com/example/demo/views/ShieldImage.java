package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the shield image displayed around the boss.
 * <p>
 * This class handles the display and visibility of the shield image,
 * including positioning and resizing.
 * </p>
 */

public class ShieldImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";

	public static final int SHIELD_SIZE = 200;

	/**
	 * Constructs a {@code ShieldImage} at the specified position.
	 *
	 * @param xPosition the X-coordinate for the shield's position
	 * @param yPosition the Y-coordinate for the shield's position
	 */
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


	/**
	 * Makes the shield visible and brings it to the front.
	 */
	public void showShield() {
		System.out.println("Showing shield");
		this.setVisible(true);
		this.toFront();
	}

	/**
	 * Hides the shield and ensures it stays in front of other elements.
	 */
	
	public void hideShield() {
		System.out.println("Hiding shield");
		this.setVisible(false);
		this.toFront();
	}

}
