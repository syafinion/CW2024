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
		super(root, heartsToDisplay);
		this.root = root;

		// Initialize shield
		this.shieldImage = new ShieldImage(1150, 200); // Position above boss

		double healthBarXPosition = HEART_DISPLAY_X_POSITION + 900; // Adjust this value as needed
		double healthBarYPosition = HEART_DISPLAY_Y_POSITION;

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
//	public void updateShieldPosition(double x, double y) {
//		if (shieldImage != null) {
//			// Adjust X and Y to align with the boss
//			shieldImage.setLayoutX(x - (SHIELD_SIZE / 2)); // Center the shield
//			shieldImage.setLayoutY(y - SHIELD_SIZE); // Position above the boss
////			shieldImage.setFitHeight(300); // Larger size for visibility
////			shieldImage.setFitWidth(300);
////			shieldImage.setLayoutX(500); // Test static position
////			shieldImage.setLayoutY(200);
//			System.out.println("Shield repositioned to: (" + (x - (SHIELD_SIZE / 2)) + ", " + (y - SHIELD_SIZE) + ")");
//		}
//	}

//	public void updateShieldPosition(double bossX, double bossY) {
//		if (shieldImage != null) {
//			// Adjust X and Y to center the shield on the boss plane
//			double shieldCenterX = bossX - (SHIELD_SIZE / 2);
//			double shieldCenterY = bossY - (SHIELD_SIZE / 2);
//			shieldImage.setLayoutX(shieldCenterX);
//			shieldImage.setLayoutY(shieldCenterY);
//
//			System.out.println("Shield repositioned to: (" + shieldCenterX + ", " + shieldCenterY + ")");
//		}
//	}

	public void updateShieldPosition(double bossX, double bossY) {
		if (shieldImage != null) {
			// Adjust X and Y to position the shield slightly in front of the boss plane
			double shieldFrontX = bossX + 1; // Move shield slightly in front (increase X)
			double shieldFrontY = bossY - (SHIELD_SIZE / 4); // Adjust vertically for better alignment

			shieldImage.setLayoutX(shieldFrontX);
			shieldImage.setLayoutY(shieldFrontY);

			System.out.println("Shield repositioned to: (" + shieldFrontX + ", " + shieldFrontY + ")");
		}
	}

//	public void updateShieldPosition(double bossX, double bossY) {
//		if (shieldImage != null) {
//			// Position the shield slightly in front of the jet
//			double shieldFrontX = bossX + (SHIELD_SIZE / 4); // Shift the shield forward
//			double shieldCenterY = bossY - (SHIELD_SIZE / 2); // Center vertically with the jet
//
//			shieldImage.setLayoutX(shieldFrontX);
//			shieldImage.setLayoutY(shieldCenterY);
//
//			System.out.println("Shield repositioned to: (" + shieldFrontX + ", " + shieldCenterY + ")");
//		}
//	}







}
