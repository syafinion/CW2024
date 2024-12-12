package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import com.example.demo.actors.UserPlane;
import com.example.demo.views.LevelView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import com.example.demo.levels.LevelParent;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";
	private final Stage stage;
	private MediaPlayer mediaPlayer;
	private LevelParent currentLevel;

	private double currentVolume = 0.5; // Default to 50%
	private double pendingGunshotVolume = 0.5;

	public Controller(Stage stage) {
		this.stage = stage;
		playBackgroundMusic();
	}

	public double getPendingGunshotVolume() {
		return pendingGunshotVolume;
	}
	public void setVolume(double volume) {
		currentVolume = volume; // Update global volume setting
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(volume);
		}
		System.out.println("Volume set to: " + (int) (volume * 100) + "%");
	}

	public void setPendingGunshotVolume(double volume) {
		this.pendingGunshotVolume = volume;
		System.out.println("Pending gunshot volume set to: " + (volume * 100) + "%");
	}

	public void applyPendingGunshotVolume() {
		UserPlane userPlane = getUserPlane();
		if (userPlane != null) {
			userPlane.setGunshotVolume(pendingGunshotVolume);
			System.out.println("Pending gunshot volume applied: " + (pendingGunshotVolume * 100) + "%");
		} else {
			System.err.println("Failed to apply pending gunshot volume. UserPlane is still null.");
		}
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Get the screen bounds
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

		// Set the stage to fit the screen bounds
		stage.setX(screenBounds.getMinX());
		stage.setY(screenBounds.getMinY());
		stage.setWidth(screenBounds.getWidth());
		stage.setHeight(screenBounds.getHeight());

		// Lock the stage in place and make it non-resizable
		stage.setResizable(false);
		stage.setFullScreen(false); // Windowed fullscreen

		// Ensure the scene uses the full stage dimensions
		stage.setScene(new Scene(new Pane())); // Temporary scene to ensure no gaps

		stage.show();

		// Show the main menu
		showMainMenu();
	}

	private void playBackgroundMusic() {
		String loopMusicPath = new File("src/main/resources/com/example/demo/images/8-bit-loop-189494.mp3").toURI().toString();

		Media loopMedia = new Media(loopMusicPath);
		MediaPlayer loopMediaPlayer = new MediaPlayer(loopMedia);

		loopMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		loopMediaPlayer.setVolume(currentVolume); // Apply global volume

		mediaPlayer = loopMediaPlayer;
		mediaPlayer.play();
	}




	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
	}
	public UserPlane getUserPlane() {
		if (currentLevel == null) {
			System.err.println("Current level is null, UserPlane cannot be retrieved.");
			return null;
		}

		UserPlane userPlane = currentLevel.getUser();
		if (userPlane == null) {
			System.err.println("UserPlane instance is null in the current level.");
		}

		return userPlane;
	}


	public void exitGame() {
		stopMusic();
		System.exit(0);
	}

	public LevelView getCurrentLevelView() {
		if (currentLevel != null) {
			return currentLevel.getLevelView();
		}
		return null;
	}

	public LevelParent getCurrentLevel() {
		return currentLevel;
	}


	private void showMainMenu() {
		MainMenu menu = new MainMenu(this);
		Scene menuScene = menu.createMenuScene();
		stage.setScene(menuScene);
	}

	public void startLevel() throws Exception {
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}


	public void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (className.equals("MAIN_MENU")) {
			showMainMenu();
			return;
		}
		if (currentLevel != null) {
			currentLevel.stop();
		}
		Class<?> myClass = Class.forName(className);
		var constructor = myClass.getConstructor(double.class, double.class, Controller.class);
		currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), this);
		currentLevel.addObserver((observable, arg) -> {
			try {
				goToLevel((String) arg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// Apply pending gunshot volume to the UserPlane
		applyPendingGunshotVolume();

		Scene scene = currentLevel.initializeScene();
		stage.setScene(scene);
		currentLevel.startGame();
	}




	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	public Stage getStage() {
		return stage;
	}


}
