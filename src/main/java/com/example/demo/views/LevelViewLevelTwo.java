package com.example.demo.views;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.example.demo.views.ShieldImage.SHIELD_SIZE;

public class LevelViewLevelTwo extends LevelView {

	// Updated constants for debugging purposes
	private static final int HEALTH_BAR_WIDTH = 300; // Standard width
	private static final int HEALTH_BAR_HEIGHT = 20; // Standard height
	private final Rectangle shieldHealthBar;
	private final Rectangle shieldHealthBarBorder;
	private final Group root;
	private final ShieldImage shieldImage;
	private final Rectangle healthBar;
	private final Rectangle healthBarBorder;

	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay, true);
		this.root = root;

		// Initialize shield image
		this.shieldImage = new ShieldImage(600, 100); // Position above boss

		// Initialize health bar and shield health bar positions
		double healthBarXPosition = root.getScene().getWidth() * 0.7; // 70% from the left
		double healthBarYPosition = root.getScene().getHeight() * 0.1; // 10% from the top

		// Initialize health bar and border
		this.healthBarBorder = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		this.healthBarBorder.setStroke(Color.BLACK);
		this.healthBarBorder.setFill(Color.TRANSPARENT); // Transparent inside, visible border
		this.healthBarBorder.setLayoutX(healthBarXPosition);
		this.healthBarBorder.setLayoutY(healthBarYPosition);

		this.healthBar = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, Color.RED);
		this.healthBar.setLayoutX(healthBarXPosition);
		this.healthBar.setLayoutY(healthBarYPosition);

		// Initialize shield health bar and border
		this.shieldHealthBarBorder = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		this.shieldHealthBarBorder.setStroke(Color.BLUE);
		this.shieldHealthBarBorder.setFill(Color.TRANSPARENT); // Transparent inside, visible border
		this.shieldHealthBarBorder.setLayoutX(healthBarXPosition);
		this.shieldHealthBarBorder.setLayoutY(healthBarYPosition + 30); // Positioned below boss health bar

		this.shieldHealthBar = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, Color.CYAN);
		this.shieldHealthBar.setLayoutX(healthBarXPosition);
		this.shieldHealthBar.setLayoutY(healthBarYPosition + 30);

		// Add elements to the root
		addElementsToRoot();
	}

	public void updateShieldHealthBar(int currentHealth, int maxHealth) {
		double healthPercentage = (double) currentHealth / maxHealth;
		double newWidth = HEALTH_BAR_WIDTH * healthPercentage;

		// Update the shield health bar width
		shieldHealthBar.setWidth(newWidth);

		if (!root.getChildren().contains(shieldHealthBar)) {
			root.getChildren().add(shieldHealthBar);
		}
		if (!root.getChildren().contains(shieldHealthBarBorder)) {
			root.getChildren().add(shieldHealthBarBorder);
		}

		// Bring the shield health bar to the front
		shieldHealthBar.toFront();
		shieldHealthBarBorder.toFront();
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
		if (!root.getChildren().contains(shieldHealthBarBorder)) {
			root.getChildren().add(shieldHealthBarBorder);
		}
		if (!root.getChildren().contains(shieldHealthBar)) {
			root.getChildren().add(shieldHealthBar);
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
