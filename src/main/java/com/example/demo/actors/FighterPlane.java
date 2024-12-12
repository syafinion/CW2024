package com.example.demo.actors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public abstract class FighterPlane extends ActiveActorDestructible {

	public int health;

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	public abstract ActiveActorDestructible fireProjectile();

	@Override
	public void takeDamage() {
		health--;
		showHitEffect(); // Add this to show the hit effect
		if (healthAtZero()) {
			this.destroy();
		}
	}

	private void showHitEffect() {
		// Create a flash effect using a timeline
		Timeline flashEffect = new Timeline(
				new KeyFrame(Duration.seconds(0), e -> this.setStyle("-fx-opacity: 0.5; -fx-effect: dropshadow(gaussian, white, 30, 0.8, 0, 0);")),
				new KeyFrame(Duration.seconds(0.1), e -> this.setStyle("-fx-opacity: 1.0; -fx-effect: none;"))
		);
		flashEffect.setCycleCount(1);
		flashEffect.play();
	}

	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	private boolean healthAtZero() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}


		
}
