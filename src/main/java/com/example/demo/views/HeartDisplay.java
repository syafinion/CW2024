package com.example.demo.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


/**
 * Displays the health of the player using a series of heart images.
 * <p>
 * This class provides logic to dynamically manage the number of hearts
 * displayed, representing the player's remaining health.
 * </p>
 */
public class HeartDisplay {

	/** Path to the heart image file. */
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	/** The height of each heart image. */
	private static final int HEART_HEIGHT = 50;
	/** Index of the first heart in the container (used for removal). */
	private static final int INDEX_OF_FIRST_ITEM = 0;
	/** The container for holding heart images. */
	private HBox container;
	/** The X position of the container. */
	private double containerXPosition;
	/** The Y position of the container. */
	private double containerYPosition;
	/** The number of hearts to display initially. */
	private int numberOfHeartsToDisplay;

	/**
	 * Constructs a new {@code HeartDisplay}.
	 *
	 * @param xPosition the X-coordinate of the heart display container
	 * @param yPosition the Y-coordinate of the heart display container
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}
	/**
	 * Initializes the container (an {@link HBox}) that holds the heart images.
	 */
	
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);		
	}
	/**
	 * Adds the initial set of hearts to the container.
	 */
	private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));

			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
	}
	/**
	 * Removes one heart from the container, representing damage to the player.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty())
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
	}

	/**
	 * Retrieves the container that holds the heart images.
	 *
	 * @return the {@link HBox} containing the heart images
	 */
	
	public HBox getContainer() {
		return container;
	}

}
