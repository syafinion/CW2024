package com.example.demo.controller;


import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

/**
 * Represents the pause menu displayed during the game.
 * Provides options to resume, restart, or return to the main menu.
 */
public class PauseMenu {
    private final Pane root;
    /**
     * Constructs the PauseMenu instance.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @param resumeAction the action to resume the game
     * @param restartAction the action to restart the game
     * @param mainMenuAction the action to return to the main menu
     */
    public PauseMenu(double screenWidth, double screenHeight, Runnable resumeAction, Runnable restartAction, Runnable mainMenuAction) {
        this.root = createPauseMenuPane(screenWidth, screenHeight, resumeAction, restartAction, mainMenuAction);
    }
    /**
     * Gets the root Pane of the pause menu.
     *
     * @return the root Pane
     */
    public Pane getRoot() {
        return root;
    }
    /**
     * Creates the pause menu layout.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @param resumeAction the action to resume the game
     * @param restartAction the action to restart the game
     * @param mainMenuAction the action to return to the main menu
     * @return the Pane representing the pause menu
     */
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
    /**
     * Represents the title section of the pause menu.
     */
    private static class Title extends StackPane {
        private final double customPrefWidth = 375;
        private final double customPrefHeight = 60;
        /**
         * Constructs the Title instance.
         *
         * @param name the title text
         */
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
        /**
         * Gets the preferred width of the title.
         *
         * @return the preferred width
         */
        public double getCustomPrefWidth() {
            return customPrefWidth;
        }
        /**
         * Gets the preferred height of the title.
         *
         * @return the preferred height
         */
        public double getCustomPrefHeight() {
            return customPrefHeight;
        }
    }

    /**
     * Represents the menu box containing the menu options.
     */
    private static class MenuBox extends VBox {
        private final double customWidth = 220; // Define a custom width for the menu box
        /**
         * Constructs the MenuBox instance.
         *
         * @param items the menu items to be added
         */
        public MenuBox(MenuItem... items) {
            setAlignment(Pos.CENTER);
            setSpacing(10); // Add spacing between menu items
            getChildren().add(createSeparator());
            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }
        /**
         * Creates a separator line between menu items.
         *
         * @return the Line object representing the separator
         */
        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(210);
            sep.setStroke(Color.DARKGREY);
            return sep;
        }
        /**
         * Gets the custom width of the menu box.
         *
         * @return the custom width
         */
        public double getCustomWidth() {
            return customWidth; // Return the custom width for centering
        }
    }

    /**
     * Represents an individual menu item.
     */
    private static class MenuItem extends StackPane {
        /**
         * Constructs the MenuItem instance.
         *
         * @param name   the name of the menu item
         * @param action the action to perform when the menu item is clicked
         */
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
