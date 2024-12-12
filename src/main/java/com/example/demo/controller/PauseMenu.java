package com.example.demo.controller;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class PauseMenu {
    private final Pane root;

    public PauseMenu(double screenWidth, double screenHeight, Runnable resumeAction, Runnable restartAction, Runnable mainMenuAction) {
        this.root = createPauseMenuPane(screenWidth, screenHeight, resumeAction, restartAction, mainMenuAction);
    }

    public Pane getRoot() {
        return root;
    }

    private Pane createPauseMenuPane(double screenWidth, double screenHeight, Runnable resumeAction, Runnable restartAction, Runnable mainMenuAction) {
        Pane pausePane = new Pane();
        pausePane.setPrefSize(screenWidth, screenHeight);

        // Black transparent background
        Rectangle bg = new Rectangle(screenWidth, screenHeight);
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.7);

        // Title
        Title title = new Title("Paused");
        title.layoutXProperty().bind(pausePane.widthProperty().subtract(title.getCustomPrefWidth()).divide(2));
        title.layoutYProperty().bind(pausePane.heightProperty().divide(4));

        // Menu Options
        MenuBox menuBox = new MenuBox(
                new MenuItem("Resume", resumeAction),
                new MenuItem("Restart", restartAction),
                new MenuItem("Main Menu", mainMenuAction)
        );

        menuBox.layoutXProperty().bind(pausePane.widthProperty().subtract(menuBox.getCustomWidth()).divide(2));

        menuBox.layoutYProperty().bind(pausePane.heightProperty().divide(2));

        pausePane.getChildren().addAll(bg, title, menuBox);
        return pausePane;
    }

    private static class Title extends StackPane {
        private final double customPrefWidth = 375;
        private final double customPrefHeight = 60;

        public Title(String name) {
            Rectangle bg = new Rectangle(customPrefWidth, customPrefHeight);
            bg.setStroke(Color.YELLOW); // Retro yellow outline
            bg.setStrokeWidth(4);
            bg.setFill(Color.BLACK); // Retro-style black background

            Text text = new Text(name);
            text.setFill(Color.YELLOW); // Bright retro color
            text.setFont(Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 40)); // Retro font

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }

        public double getCustomPrefWidth() {
            return customPrefWidth;
        }

        public double getCustomPrefHeight() {
            return customPrefHeight;
        }
    }


    private static class MenuBox extends VBox {
        private final double customWidth = 220; // Define a custom width for the menu box

        public MenuBox(MenuItem... items) {
            setAlignment(Pos.CENTER);
            setSpacing(10); // Add spacing between menu items
            getChildren().add(createSeparator());
            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }

        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(210);
            sep.setStroke(Color.DARKGREY);
            return sep;
        }

        public double getCustomWidth() {
            return customWidth; // Return the custom width for centering
        }
    }


    private static class MenuItem extends StackPane {
        public MenuItem(String name, Runnable action) {
            Rectangle bg = new Rectangle(220, 40);
            bg.setFill(Color.BLACK); // Retro-style black background
            bg.setStroke(Color.YELLOW); // Yellow outline for retro style
            bg.setStrokeWidth(2);

            Text text = new Text(name);
            text.setFill(Color.YELLOW);
            text.setFont(Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 20));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            // Hover and selection effects
            setOnMouseEntered(event -> {
                bg.setFill(Color.DARKGRAY);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setFill(Color.BLACK);
                text.setFill(Color.YELLOW);
            });

            setOnMouseClicked(event -> {
                action.run(); // Perform the action
            });
        }
    }

}
