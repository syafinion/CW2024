package com.example.demo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

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

	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);

		// Initialize kill count display
		this.killCountDisplay = new Text("Kills: 0");
		this.killCountDisplay.setFont(new Font(20));
		this.killCountDisplay.setFill(Color.WHITE);

		// Add heart display and kill count display to root
		root.getChildren().add(this.killCountDisplay);
	}
	
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}


//	public void updateKillCountDisplay(int kills) {
//		killCountDisplay.setText("Kills: " + kills);
//
//		// Ensure layout bounds are recalculated for accurate positioning
//		killCountDisplay.applyCss();
//		killCountDisplay.layoutXProperty();
//
//		// Position the kill count text relative to the heart display
//		double heartDisplayWidth = heartDisplay.getContainer().getBoundsInParent().getWidth();
//		double killCountXPosition = HEART_DISPLAY_X_POSITION + heartDisplayWidth + 20; // Offset by 20 pixels to the right
//		double killCountYPosition = HEART_DISPLAY_Y_POSITION + 5; // Align slightly lower than the hearts
//
//		killCountDisplay.setLayoutX(killCountXPosition);
//		killCountDisplay.setLayoutY(killCountYPosition);
//
//		// Debugging log to verify positioning
//		System.out.println("Kill count positioned at X=" + killCountXPosition + ", Y=" + killCountYPosition + " with value: " + kills);
//
//		// Ensure the kill count display is added to the root
//		if (!root.getChildren().contains(killCountDisplay)) {
//			root.getChildren().add(killCountDisplay);
//			System.out.println("Kill count display added to the scene.");
//		}
//	}

	public void updateKillCountDisplay(int kills) {
		killCountDisplay.setText("Kills: " + kills);

		// Increase font size for visibility
		killCountDisplay.setFont(new Font(40)); // Larger font for testing

		// Manual offsets for adjustment
		double xOffset = 1; // Adjust this value for horizontal positioning
		double yOffset = 40; // Adjust this value for vertical positioning

		// Calculate positions dynamically
		double sceneWidth = root.getScene().getWidth();
		double killCountTextWidth = killCountDisplay.getLayoutBounds().getWidth();
		double killCountXPosition = (sceneWidth - killCountTextWidth) / 2 + xOffset; // Center horizontally and apply offset
		double killCountYPosition = HEART_DISPLAY_Y_POSITION + yOffset; // Same vertical position as hearts and apply offset

		// Set position for kill count display
		killCountDisplay.setLayoutX(killCountXPosition);
		killCountDisplay.setLayoutY(killCountYPosition);

		// Ensure the kill count display is added to the root
		if (!root.getChildren().contains(killCountDisplay)) {
			root.getChildren().add(killCountDisplay);
			System.out.println("Kill count display added to the scene.");
		}

		// Bring the kill count display to the front
		killCountDisplay.toFront();

		// Debugging log to verify positioning
		System.out.println("Kill count positioned at X=" + killCountXPosition + ", Y=" + killCountYPosition + " with value: " + kills);
	}









	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}
	
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}
	
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

}
