package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(TITLE);
		stage.setResizable(false);

		// Set default to fullscreen mode
		stage.setFullScreen(true);

		// Create controller and start game
		Controller controller = new Controller(stage);
		controller.launchGame();
	}


	public static void main(String[] args) {
		launch();
	}
}