package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import com.example.demo.LevelView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import com.example.demo.LevelParent;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;
	private MediaPlayer mediaPlayer;
	private LevelParent currentLevel;

	public Controller(Stage stage) {
		this.stage = stage;
		playBackgroundMusic();
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
		// Path to the first and second music files
		String firstMusicPath = new File("src/main/resources/com/example/demo/images/boom-8-bit-36004.mp3").toURI().toString();
		String loopMusicPath = new File("src/main/resources/com/example/demo/images/8-bit-loop-189494.mp3").toURI().toString();

		// Create the first MediaPlayer
		Media firstMedia = new Media(firstMusicPath);
		MediaPlayer firstMediaPlayer = new MediaPlayer(firstMedia);

		// Create the loop MediaPlayer
		Media loopMedia = new Media(loopMusicPath);
		MediaPlayer loopMediaPlayer = new MediaPlayer(loopMedia);

		// Set the loop MediaPlayer to repeat
		loopMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		// Play the loop track after the first track finishes
		firstMediaPlayer.setOnEndOfMedia(() -> {
			firstMediaPlayer.dispose(); // Clean up resources
			loopMediaPlayer.play();    // Start the loop track
			mediaPlayer = loopMediaPlayer;
		});

		// Start playing the first track
		mediaPlayer = firstMediaPlayer;
		mediaPlayer.play();
	}

	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
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


	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (className.equals("MAIN_MENU")) {
			showMainMenu(); // Call the method to show the main menu directly
			return;
		}
		if (currentLevel != null) {
			currentLevel.stop(); // Stop the timeline of the current level
		}
		Class<?> myClass = Class.forName(className);
		var constructor = myClass.getConstructor(double.class, double.class);
		currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		currentLevel.addObserver((observable, arg) -> {
			try {
				goToLevel((String) arg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
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
