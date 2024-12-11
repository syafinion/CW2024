package com.example.demo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

import static com.example.demo.ShieldImage.SHIELD_SIZE;

public class LevelViewLevelTwo extends LevelView {

	// Updated constants for debugging purposes
	private static final int HEALTH_BAR_WIDTH = 300; // Standard width
	private static final int HEALTH_BAR_HEIGHT = 20; // Standard height

	private final Group root;
	private final ShieldImage shieldImage;
	private final Rectangle healthBar;
	private final Rectangle healthBarBorder;

	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay, true);
		this.root = root;

		// Initialize shield
		this.shieldImage = new ShieldImage(600, 100); // Position above boss

		double healthBarXPosition = root.getScene().getWidth() * 0.7; // 70% from the left
		double healthBarYPosition = root.getScene().getHeight() * 0.1; // 10% from the top


		// Initialize health bar border
		this.healthBarBorder = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		this.healthBarBorder.setStroke(Color.BLACK);
		this.healthBarBorder.setFill(Color.TRANSPARENT); // Transparent inside, visible border
		this.healthBarBorder.setLayoutX(healthBarXPosition);
		this.healthBarBorder.setLayoutY(healthBarYPosition);

		// Initialize health bar
		this.healthBar = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, Color.RED);
		this.healthBar.setLayoutX(healthBarXPosition);
		this.healthBar.setLayoutY(healthBarYPosition);

		addElementsToRoot();
	}

	public void adjustHealthBarPosition() {
		double healthBarXPosition = root.getScene().getWidth() * 0.7;
		double healthBarYPosition = root.getScene().getHeight() * 0.1;

		healthBar.setLayoutX(healthBarXPosition);
		healthBar.setLayoutY(healthBarYPosition);
		healthBarBorder.setLayoutX(healthBarXPosition);
		healthBarBorder.setLayoutY(healthBarYPosition);
	}


	private void addElementsToRoot() {
		if (!root.getChildren().contains(healthBarBorder)) {
			root.getChildren().add(healthBarBorder);
		}
		if (!root.getChildren().contains(healthBar)) {
			root.getChildren().add(healthBar);
		}
		if (!root.getChildren().contains(shieldImage)) {
			root.getChildren().add(shieldImage);
		}
	}



	public void updateBossHealthBar(int currentHealth, int maxHealth) {
		double healthPercentage = (double) currentHealth / maxHealth;
		double newWidth = HEALTH_BAR_WIDTH * healthPercentage;

		// Update the health bar width
		healthBar.setWidth(newWidth);

		// Debugging information
		System.out.println("Updating Boss Health Bar:");
		System.out.println(" - Current Health: " + currentHealth);
		System.out.println(" - Max Health: " + maxHealth);
		System.out.println(" - Health Percentage: " + (healthPercentage * 100) + "%");
		System.out.println(" - New Health Bar Width: " + newWidth);

		// Ensure the health bar is added and visible
		if (!root.getChildren().contains(healthBar)) {
			root.getChildren().add(healthBar);
		}
		if (!root.getChildren().contains(healthBarBorder)) {
			root.getChildren().add(healthBarBorder);
		}

		// Bring the health bar to the front
		healthBar.toFront();
		healthBarBorder.toFront();
	}



	public void showShield() {
		System.out.println("Showing shield");
		shieldImage.showShield();
	}

	public void hideShield() {
		System.out.println("Hiding shield");
		shieldImage.hideShield();
	}

	@Override
	public void updateShieldPosition(double bossX, double bossY) {
		if (shieldImage != null) {
			// Position the shield relative to the boss plane's current location
			double shieldX = bossX + 1; // Slightly in front of the boss plane
			double shieldY = bossY - (SHIELD_SIZE / 4); // Adjust vertically for better alignment

			// Apply the calculated position to the shield image
			shieldImage.setLayoutX(shieldX);
			shieldImage.setLayoutY(shieldY);

			System.out.println("Shield repositioned to: (" + shieldX + ", " + shieldY + ")");
		}
	}



}
