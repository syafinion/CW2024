package com.example.demo.controller;

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
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(2);
            bg.setFill(null);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 50));

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
            Rectangle bg = new Rectangle(200, 30);
            bg.setOpacity(0.4);
            bg.setFill(Color.BLACK);

            Text text = new Text(name);
            text.setFill(Color.DARKGREY);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 20));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setFill(Color.GRAY);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setFill(Color.BLACK);
                text.setFill(Color.DARKGREY);
            });

            setOnMouseClicked(event -> {
                action.run(); // Perform the action associated with this item
            });
        }
    }
}
