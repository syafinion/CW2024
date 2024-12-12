package com.example.demo.views;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LevelView {
	
	protected static final double HEART_DISPLAY_X_POSITION = 5;
	protected static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSISITION = -375;
	private static final double KILL_COUNT_Y_OFFSET = 50;
	private final Text killCountDisplay;
	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;

	private final boolean isLevelThree;





	public LevelView(Group root, int heartsToDisplay, boolean isLevelThree) {
		this.root = root;
		this.isLevelThree = isLevelThree;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);

		this.killCountDisplay = new Text("Kills: 0");
		this.killCountDisplay.setFont(new Font(20));
		this.killCountDisplay.setFill(Color.WHITE);

		root.getChildren().add(this.killCountDisplay);
	}

	private boolean isLevelThree() {
		return this.isLevelThree;
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}


	public void updateKillCountDisplay(int kills) {
		if (isLevelThree()) {
			// Skip updating the kill count for LevelThree
			return;
		}

		// Update kill count text
		killCountDisplay.setText("Kills: " + kills);

		// Apply the retro font (ensure the font file is loaded in your project resources)
		killCountDisplay.setFont(Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 30));
		killCountDisplay.setFill(Color.YELLOW); // Use a bright color for better visibility

		// Center the kill count text dynamically
		double killCountXPosition = root.getScene().getWidth() * 0.5 - killCountDisplay.getLayoutBounds().getWidth() / 2;
		double killCountYPosition = root.getScene().getHeight() * 0.05;

		killCountDisplay.setLayoutX(killCountXPosition);
		killCountDisplay.setLayoutY(killCountYPosition);

		// Ensure the kill count display is added to the root
		if (!root.getChildren().contains(killCountDisplay)) {
			root.getChildren().add(killCountDisplay);
		}

		// Add visual effects: scale and fade animation
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), killCountDisplay);
		scaleTransition.setFromX(1.0);
		scaleTransition.setFromY(1.0);
		scaleTransition.setToX(1.5);
		scaleTransition.setToY(1.5);
		scaleTransition.setAutoReverse(true);

		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), killCountDisplay);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.7);
		fadeTransition.setAutoReverse(true);

		// Play animations simultaneously
		scaleTransition.play();
		fadeTransition.play();

		// Bring the kill count display to the front
		killCountDisplay.toFront();

		// Debugging log
		System.out.println("Kill count positioned at X=" + killCountXPosition + ", Y=" + killCountYPosition + " with value: " + kills);
	}



	public void adjustKillCountPosition() {
		double killCountXPosition = root.getScene().getWidth() * 0.5 - killCountDisplay.getLayoutBounds().getWidth() / 2;
		double killCountYPosition = root.getScene().getHeight() * 0.05;

		killCountDisplay.setLayoutX(killCountXPosition);
		killCountDisplay.setLayoutY(killCountYPosition);
	}



	public void adjustPositionsForResolution() {
		adjustHealthBarPosition();
		adjustKillCountPosition();
		updateShieldPosition(0, 0); // Recalculates shield position
	}

	public void adjustHealthBarPosition() {
		// Default implementation: Do nothing
		System.out.println("adjustHealthBarPosition is not implemented in LevelView.");
	}

	public void updateShieldPosition(double bossX, double bossY) {
		// Default implementation: Do nothing
		System.out.println("updateShieldPosition is not implemented in LevelView.");
	}



//	public void showWinImage() {
//		// Check if the WinImage is already added to the root
//		if (!root.getChildren().contains(winImage)) {
//			root.getChildren().add(winImage);
//		}
//		winImage.showWinImage();
//	}



	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		System.out.println("Current hearts: " + currentNumberOfHearts + ", Hearts remaining: " + heartsRemaining);
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}


}
