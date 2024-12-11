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
import javafx.stage.Stage;
import com.example.demo.LevelParent;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;

	private LevelParent currentLevel;

	public Controller(Stage stage) {
		this.stage = stage;
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
