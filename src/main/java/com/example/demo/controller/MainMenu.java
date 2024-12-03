package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainMenu {
    private final Controller controller;
    private static final String IMAGE_NAME = "/com/example/demo/images/battlejet.gif";

    private Pane root;
    private MenuBox vbox;
    private Pane settingsPane;

    private int pendingWidth;
    private int pendingHeight;
    private boolean pendingFullscreen;

    public MainMenu(Controller controller) {
        this.controller = controller;
    }

    public Scene createMenuScene() {
        root = new Pane();
        root.setPrefSize(1300, 750);

        // Add Background Image
        try {
            ImageView img = new ImageView(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
            img.fitWidthProperty().bind(root.widthProperty());
            img.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(img);
        } catch (NullPointerException e) {
            System.err.println("Image file not found: " + IMAGE_NAME);
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        // Title
        Title title = new Title("Sky Battle");
        title.layoutXProperty().bind(root.widthProperty().subtract(title.getCustomPrefWidth()).divide(2));
        title.layoutYProperty().bind(root.heightProperty().divide(8)); // Adjusted for better centering

        // Menu Items
        vbox = new MenuBox( // Assign to instance variable
                new MenuItem("START GAME", () -> {
                    try {
                        controller.startLevel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }),
                new MenuItem("SETTINGS", () -> toggleSettings(root)),
                new MenuItem("EXIT", () -> System.exit(0))
        );
        vbox.layoutXProperty().bind(root.widthProperty().subtract(vbox.widthProperty()).divide(2));
        vbox.layoutYProperty().bind(root.heightProperty().divide(2.5));

        // Settings Pane
        Pane settingsPane = createSettingsPane();
        settingsPane.setVisible(false);
        settingsPane.layoutXProperty().bind(root.widthProperty().subtract(settingsPane.prefWidthProperty()).divide(2));
        settingsPane.layoutYProperty().bind(root.heightProperty().subtract(settingsPane.prefHeightProperty()).divide(2));

        root.getChildren().addAll(title, vbox, settingsPane);
        return new Scene(root);
    }


    private Pane createSettingsPane() {
        Pane settingsPane = new Pane();
        settingsPane.setPrefSize(500, 300); // Increase height to accommodate buttons

        // Background for settings pane
        Rectangle bg = new Rectangle(500, 300);
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.8);

        // Title for settings
        Text settingsTitle = new Text("SETTINGS");
        settingsTitle.setFill(Color.WHITE);
        settingsTitle.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 30));
        settingsTitle.setX(200);
        settingsTitle.setY(50);

        // Resolution options
        MenuBox resolutionMenu = new MenuBox(
                new MenuItem("1280 x 720 (Windowed)", () -> setPendingResolution(1280, 720, false)),
                new MenuItem("1600 x 900 (Windowed)", () -> setPendingResolution(1600, 900, false)),
                new MenuItem("1920 x 1080 (Windowed)", () -> setPendingResolution(1920, 1080, false)),
                new MenuItem("Toggle Fullscreen", this::togglePendingFullscreen)
        );
        resolutionMenu.setLayoutX(100);
        resolutionMenu.setLayoutY(80);

        // Add Apply and Cancel buttons
        StackPane applyButton = createButton("Apply", () -> applyResolution(settingsPane));
        StackPane cancelButton = createButton("Cancel", () -> closeSettings(settingsPane));

        applyButton.setLayoutX(150);
        applyButton.setLayoutY(220);

        cancelButton.setLayoutX(300);
        cancelButton.setLayoutY(220);

        settingsPane.getChildren().addAll(bg, settingsTitle, resolutionMenu, applyButton, cancelButton);
        return settingsPane;
    }

    private StackPane createButton(String name, Runnable action) {
        StackPane button = new StackPane();
        button.setPrefSize(100, 30);

        Rectangle bg = new Rectangle(100, 30);
        bg.setFill(Color.DARKGRAY);

        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        button.getChildren().addAll(bg, text);

        button.setOnMouseEntered(e -> bg.setFill(Color.GRAY));
        button.setOnMouseExited(e -> bg.setFill(Color.DARKGRAY));
        button.setOnMouseClicked(e -> action.run());

        return button;
    }


    private void setPendingResolution(int width, int height, boolean fullscreen) {
        pendingWidth = width;
        pendingHeight = height;
        pendingFullscreen = fullscreen;
        System.out.println("Pending resolution set to: " + width + "x" + height + " Fullscreen: " + fullscreen);
    }

    private void togglePendingFullscreen() {
        pendingFullscreen = !pendingFullscreen;
        System.out.println("Pending fullscreen: " + pendingFullscreen);
    }

    private void applyResolution(Pane settingsPane) {
        changeResolution(pendingWidth, pendingHeight, pendingFullscreen);
        settingsPane.setVisible(false); // Close settings pane
        System.out.println("Resolution applied: " + pendingWidth + "x" + pendingHeight + " Fullscreen: " + pendingFullscreen);
    }

    private void closeSettings(Pane settingsPane) {
        settingsPane.setVisible(false);
        System.out.println("Settings pane closed without changes.");
    }



    private void changeResolution(int width, int height, boolean fullscreen) {
        Stage stage = controller.getStage();
        stage.setFullScreen(fullscreen);

        if (!fullscreen) {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.centerOnScreen(); // Center the window
        }

        adjustLayouts(); // Immediately adjust layouts after resolution change

        System.out.println("Resolution changed to: " + width + "x" + height + " Fullscreen: " + fullscreen);
    }



    private void adjustLayouts() {
        if (vbox != null) {
            vbox.layoutXProperty().bind(root.widthProperty().subtract(vbox.widthProperty()).divide(2)); // Dynamically bind to the vbox width
            vbox.layoutYProperty().bind(root.heightProperty().divide(2.5)); // Adjust vertical position
        }

        if (settingsPane != null) {
            settingsPane.layoutXProperty().bind(root.widthProperty().subtract(settingsPane.prefWidthProperty()).divide(2)); // Bind to the prefWidth
            settingsPane.layoutYProperty().bind(root.heightProperty().subtract(settingsPane.prefHeightProperty()).divide(2)); // Center vertically
        }
    }


    private void toggleFullscreen() {
        Stage stage = controller.getStage();
        stage.setFullScreen(!stage.isFullScreen());
        System.out.println("Fullscreen mode: " + stage.isFullScreen());
    }


    private void toggleSettings(Pane root) {
        for (var node : root.getChildren()) {
            if (node instanceof Pane && ((Pane) node).getPrefWidth() == 500) {
                node.setVisible(!node.isVisible());

                if (node.isVisible()) {
                    // Highlight default resolution
                    ((MenuBox) ((Pane) node).getChildren().get(2))
                            .getChildren().filtered(n -> n instanceof MenuItem)
                            .stream()
                            .findFirst()
                            .ifPresent(menuItem -> ((MenuItem) menuItem).fireEvent(new javafx.event.ActionEvent()));
                }
            }
        }
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
        public MenuBox(MenuItem... items) {
            setAlignment(Pos.CENTER);
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
    }



    private static class MenuItem extends StackPane {
        private static MenuItem selectedMenuItem = null; // Static variable to track the currently selected item
        private final Rectangle bg; // Background rectangle for easier updates

        public MenuItem(String name, Runnable action) {
            bg = new Rectangle(200, 30);
            bg.setOpacity(0.4);
            bg.setFill(Color.BLACK);

            Text text = new Text(name);
            text.setFill(Color.DARKGREY);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 20));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                if (this != selectedMenuItem) {
                    bg.setFill(Color.GRAY);
                    text.setFill(Color.WHITE);
                }
            });

            setOnMouseExited(event -> {
                if (this != selectedMenuItem) {
                    bg.setFill(Color.BLACK);
                    text.setFill(Color.DARKGREY);
                }
            });

            setOnMouseClicked(event -> {
                if (selectedMenuItem != null) {
                    selectedMenuItem.bg.setFill(Color.BLACK); // Reset previous selection
                    ((Text) selectedMenuItem.getChildren().get(1)).setFill(Color.DARKGREY);
                }
                selectedMenuItem = this;
                bg.setFill(Color.DARKBLUE); // Highlight selected item
                text.setFill(Color.WHITE);
                action.run(); // Perform the action associated with this item
            });
        }
    }


}
