package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static final String TITLE = "Sky Battle";

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