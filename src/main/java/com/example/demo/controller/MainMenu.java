package com.example.demo.controller;

import com.example.demo.LevelParent;
import com.example.demo.LevelThree;
import com.example.demo.LevelView;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenu {
    private final Controller controller;
    private static final String IMAGE_NAME = "/com/example/demo/images/mainmenubg.gif";

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
        // Initialize root first to avoid NullPointerException
        root = new Pane();
        root.setPrefSize(1300, 750);

        // Add gradient background
        Stop[] stops = new Stop[] {
                new Stop(0, Color.DARKBLUE),
                new Stop(1, Color.BLACK)
        };
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );
        Rectangle bg = new Rectangle(root.getPrefWidth(), root.getPrefHeight());
        bg.setFill(gradient);

        // Add pulsing animation
        FadeTransition fade = new FadeTransition(Duration.seconds(2), bg);
        fade.setFromValue(0.8);
        fade.setToValue(1.0);
        fade.setAutoReverse(true);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.play();

        root.getChildren().add(bg);

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
                new MenuItem("EXIT", () -> {
                    controller.stopMusic();
                    System.exit(0);
                })
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


    public void addGlowEffect(Text text, Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(10);
        glow.setSpread(0.4);
        text.setEffect(glow);
    }



    private Pane createSettingsPane() {
        Pane settingsPane = new Pane();
        settingsPane.setPrefSize(700, 500); // Increased height to accommodate buttons

        // Background rectangle
        Rectangle bg = new Rectangle(700, 500); // Match the updated size
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.8);

        // Settings title
        Text settingsTitle = new Text("SETTINGS");
        settingsTitle.setFill(Color.WHITE);
        settingsTitle.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 40));

        // Center layout
        VBox centerLayout = new VBox(30); // Increased spacing between elements
        centerLayout.setAlignment(Pos.TOP_CENTER);
        centerLayout.setPrefSize(700, 450);

        // Resolution menu
        MenuBox resolutionMenu = new MenuBox(
                new MenuItem("1280 x 720 (Windowed)", () -> setPendingResolution(1280, 720, false)),
                new MenuItem("1600 x 900 (Windowed)", () -> setPendingResolution(1600, 900, false)),
                new MenuItem("1920 x 1080 (Windowed)", () -> {
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    if (screenBounds.getWidth() >= 1920 && screenBounds.getHeight() >= 1080) {
                        setPendingResolution(1920, 1080, false);
                    } else {
                        System.out.println("1920 x 1080 is not supported on this screen.");
                        setPendingResolution((int) screenBounds.getWidth(), (int) screenBounds.getHeight(), false);
                    }
                }),
                new MenuItem("Default (1300 x 750)", () -> setPendingResolution(1300, 750, false)),
                new MenuItem("Toggle Fullscreen", this::togglePendingFullscreen)
        );

        // Volume slider with label
        Text volumeLabel = new Text("Volume: 50%");
        volumeLabel.setFill(Color.WHITE);
        volumeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        javafx.scene.control.Slider volumeSlider = new javafx.scene.control.Slider(0, 1, 0.5);
        volumeSlider.setPrefWidth(200);
        volumeSlider.setValue(0.5);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double percentage = newValue.doubleValue() * 100;
            volumeLabel.setText(String.format("Volume: %.0f%%", percentage));
            controller.setVolume(newValue.doubleValue()); // Update global volume
        });


        VBox volumeControl = new VBox(10, volumeLabel, volumeSlider);
        volumeControl.setAlignment(Pos.CENTER);

        // Apply and Cancel buttons
        StackPane applyButton = createButton("Apply", () -> applyResolution(settingsPane));
        StackPane cancelButton = createButton("Cancel", () -> closeSettings(settingsPane));

        HBox buttonsLayout = new HBox(20); // Horizontal layout for buttons
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(applyButton, cancelButton);

        // Adding all elements to center layout
        centerLayout.getChildren().addAll(resolutionMenu, volumeControl, buttonsLayout);

        // Position title and center layout
        settingsTitle.setLayoutX(settingsPane.getPrefWidth() / 2 - settingsTitle.getLayoutBounds().getWidth() / 2);
        settingsTitle.setLayoutY(50);
        centerLayout.setLayoutY(100);

        // Add components to the settings pane
        settingsPane.getChildren().addAll(bg, settingsTitle, centerLayout);
        settingsPane.setVisible(false);
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
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        if (pendingWidth > screenBounds.getWidth() || pendingHeight > screenBounds.getHeight()) {
            System.out.println("The selected resolution is not supported. Falling back to maximum available resolution.");
            setPendingResolution((int) screenBounds.getWidth(), (int) screenBounds.getHeight(), pendingFullscreen);
        }

        changeResolution(pendingWidth, pendingHeight, pendingFullscreen);
        settingsPane.setVisible(false);
        adjustLayouts();// Close settings pane
        System.out.println("Resolution applied: " + pendingWidth + "x" + pendingHeight + " Fullscreen: " + pendingFullscreen);
    }


    private void closeSettings(Pane settingsPane) {
        settingsPane.setVisible(false);
        System.out.println("Settings pane closed without changes.");
    }



    private void changeResolution(int width, int height, boolean fullscreen) {
        Stage stage = controller.getStage();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        if (fullscreen) {
            stage.setFullScreen(true); // Enable fullscreen mode
        } else {
            stage.setFullScreen(false); // Disable fullscreen

            // Limit the stage size to fit within the screen bounds
            double finalWidth = Math.min(width, screenBounds.getWidth());
            double finalHeight = Math.min(height, screenBounds.getHeight());

            stage.setWidth(finalWidth);
            stage.setHeight(finalHeight);

            // Center the stage within the screen bounds
            stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - finalWidth) / 2);
            stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - finalHeight) / 2);
        }

        adjustLayouts(); // Adjust UI layouts based on the new dimensions
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

        adjustLayoutsForResolution();
    }

    private void adjustLayoutsForResolution() {
        LevelView levelView = controller.getCurrentLevelView();
        if (levelView != null) {
            levelView.adjustKillCountPosition();
            levelView.adjustHealthBarPosition();
        }

        LevelParent currentLevel = controller.getCurrentLevel();
        if (currentLevel instanceof LevelThree) {
            ((LevelThree) currentLevel).adjustBossPosition();
        }
    }




    private void toggleFullscreen() {
        Stage stage = controller.getStage();
        stage.setFullScreen(!stage.isFullScreen());
        System.out.println("Fullscreen mode: " + stage.isFullScreen());
    }


    private void toggleSettings(Pane root) {
        for (var node : root.getChildren()) {
            if (node instanceof Pane && ((Pane) node).getPrefWidth() == 700) { // Match the size of the settings pane
                node.setVisible(!node.isVisible()); // Toggle visibility
            }
        }
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

            // Add glow effect directly in the Title class
            DropShadow glow = new DropShadow();
            glow.setColor(Color.YELLOW);
            glow.setRadius(15);
            glow.setSpread(0.7);
            text.setEffect(glow);

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
        private static MenuItem selectedMenuItem = null; // Track the currently selected item
        private final Rectangle bg; // Background rectangle for easier updates

        public MenuItem(String name, Runnable action) {
            bg = new Rectangle(220, 40);
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
                if (this != selectedMenuItem) {
                    bg.setFill(Color.DARKGRAY);
                    text.setFill(Color.WHITE);
                }
            });

            setOnMouseExited(event -> {
                if (this != selectedMenuItem) {
                    bg.setFill(Color.BLACK);
                    text.setFill(Color.YELLOW);
                }
            });

            setOnMouseClicked(event -> {
                if (selectedMenuItem != null) {
                    selectedMenuItem.bg.setFill(Color.BLACK); // Reset previous selection
                    ((Text) selectedMenuItem.getChildren().get(1)).setFill(Color.YELLOW);
                }
                selectedMenuItem = this;
                bg.setFill(Color.DARKBLUE); // Highlight selected item
                text.setFill(Color.WHITE);
                action.run(); // Perform the action
            });
        }
    }



}
