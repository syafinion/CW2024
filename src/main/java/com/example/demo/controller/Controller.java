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

	private double currentVolume = 0.5; // Default to 50%


	public Controller(Stage stage) {
		this.stage = stage;
		playBackgroundMusic();
	}

	public void setVolume(double volume) {
		currentVolume = volume; // Update global volume setting
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(volume);
		}
		System.out.println("Volume set to: " + (int) (volume * 100) + "%");
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
		String firstMusicPath = new File("src/main/resources/com/example/demo/images/boom-8-bit-36004.mp3").toURI().toString();
		String loopMusicPath = new File("src/main/resources/com/example/demo/images/8-bit-loop-189494.mp3").toURI().toString();

		Media firstMedia = new Media(firstMusicPath);
		MediaPlayer firstMediaPlayer = new MediaPlayer(firstMedia);

		Media loopMedia = new Media(loopMusicPath);
		MediaPlayer loopMediaPlayer = new MediaPlayer(loopMedia);

		loopMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		firstMediaPlayer.setOnEndOfMedia(() -> {
			firstMediaPlayer.dispose();
			loopMediaPlayer.setVolume(currentVolume); // Apply global volume
			loopMediaPlayer.play();
			mediaPlayer = loopMediaPlayer;
		});

		mediaPlayer = firstMediaPlayer;
		mediaPlayer.setVolume(currentVolume); // Apply global volume
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
			showMainMenu();
			return;
		}
		if (currentLevel != null) {
			currentLevel.stop();
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

		// Apply current volume to the media player when entering a new level
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(currentVolume);
		}

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
