package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = .01;
	private final UserPlane userPlane;
	private static final String DAMAGE_IMAGE = "/com/example/demo/images/explode.png";
	private final Group root;
	private boolean hasPassedPlayer; // Flag to indicate if the jet has passed the player
	private static final double VERTICAL_SAFETY_DISTANCE = 100.0; // Distance to prevent shooting when user is underneath

	public EnemyPlane(double initialXPos, double initialYPos, UserPlane userPlane, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
		this.userPlane = userPlane; // Initialize UserPlane reference
		this.root = root; // Reference to the root group
		this.hasPassedPlayer = false; // Initialize flag
	}

	@Override
	public void updatePosition() {
		// Move the jet horizontally
		moveHorizontally(HORIZONTAL_VELOCITY);

		// Check if the jet has passed the player
		if (!hasPassedPlayer && hasPassedPlayerPosition()) {
			hasPassedPlayer = true; // Set the flag to true
		}
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		// Allow shooting only if the jet has not passed the player
		if (!hasPassedPlayer && !isUserPlaneUnderneath() && Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition, userPlane); // Pass UserPlane
		}
		return null;
	}

	@Override
	public void takeDamage() {
		super.takeDamage(); // Call the base takeDamage method
		showDamageEffect(); // Trigger the damage effect
	}

	private void showDamageEffect() {
		// Create an ImageView for the damage image
		ImageView damageEffect = new ImageView(new Image(getClass().getResource(DAMAGE_IMAGE).toExternalForm()));

		// Set the position and size to align with the EnemyPlane
		damageEffect.setFitWidth(getBoundsInParent().getWidth());
		damageEffect.setFitHeight(getBoundsInParent().getHeight());
		damageEffect.setLayoutX(getBoundsInParent().getMinX());
		damageEffect.setLayoutY(getBoundsInParent().getMinY());

		// Add the damage effect to the scene
		root.getChildren().add(damageEffect);

		// Pause before removing the damage effect
		PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // Display for 0.5 seconds
		pause.setOnFinished(e -> root.getChildren().remove(damageEffect));

		// Play the effect
		pause.play();
	}



	@Override
	public void updateActor() {
		updatePosition();
	}

	private boolean hasPassedPlayerPosition() {
		// Check if the jet has moved past the player's X position
		return this.getTranslateX() + this.getLayoutX() < userPlane.getTranslateX() + userPlane.getLayoutX();
	}


	private boolean isUserPlaneUnderneath() {
		// Check if the user plane is vertically too close underneath the enemy plane
		double enemyPlaneY = this.getTranslateY() + this.getLayoutY();
		double userPlaneY = userPlane.getTranslateY() + userPlane.getLayoutY();
		return Math.abs(enemyPlaneY - userPlaneY) < VERTICAL_SAFETY_DISTANCE;
	}
}
