package com.example.demo.levels;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.Boss;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.controller.Controller;
import com.example.demo.controller.PauseMenu;
import com.example.demo.views.*;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Abstract parent class for all game levels.
 * <p>
 * This class defines the common functionality and structure shared across all levels,
 * such as initializing the scene, managing actors, handling collisions, and tracking the game state.
 * </p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Manage enemy and user units, projectiles, and level-specific behavior.</li>
 *   <li>Define common actions like pausing, resuming, and restarting levels.</li>
 *   <li>Handle scene transitions and user interactions.</li>
 * </ul>
 */

public abstract class LevelParent extends Observable {

	/** Adjustment value for calculating the maximum Y position of enemies. */
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;

	/** Delay between game loop cycles in milliseconds. */
	private static final int MILLISECOND_DELAY = 50;
	/** Height of the game screen. */
	private final double screenHeight;
	/** Width of the game screen. */
	private final double screenWidth;
	/** Maximum Y position for enemies. */
	private final double enemyMaximumYPosition;
	/** Root group for all graphical elements in the level. */
	private final Group root;
	/** Timeline for the game loop. */
	protected final Timeline timeline;
	/** User-controlled plane. */
	private final UserPlane user;
	/** Scene representing the current level. */
	private final Scene scene;
	/** Background image for the level. */
	private final ImageView background;
	/** List of friendly units in the level. */
	private final List<ActiveActorDestructible> friendlyUnits;
	/** List of enemy units in the level. */
	public final List<ActiveActorDestructible> enemyUnits;
	/** List of projectiles fired by the user. */
	private final List<ActiveActorDestructible> userProjectiles;
	/** List of projectiles fired by enemies. */
	private final List<ActiveActorDestructible> enemyProjectiles;

	/** Current number of enemies in the level. */
	private int currentNumberOfEnemies;

	/** Level-specific UI elements. */
	private LevelView levelView;

	/** Boss entity in the level (if any). */
	protected Boss boss;
	/** Indicates if the game is paused. */
	private boolean isPaused = false;

	/** Pause menu displayed when the game is paused. */
	private PauseMenu pauseMenu;

	/** Indicates if the level is transitioning to the next level. */
	private boolean transitioningToNextLevel = false;

	/** Controller managing game logic and transitions. */
	private final Controller controller;

	/** Manager for level-specific UI components. */
	private LevelUIManager levelUIManager;

	/** Text element displaying the current level. */
	private Text levelText;

	/** Text element displaying the objective of the level. */
	private Text objectiveText;
	public UserPlane getUserPlane() {
		return user;
	}


	/**
	 * Constructs a new LevelParent instance.
	 *
	 * @param backgroundImageName the name of the background image for the level
	 * @param screenHeight the height of the game screen
	 * @param screenWidth the width of the game screen
	 * @param playerInitialHealth the initial health of the player
	 * @param controller the controller managing game logic and transitions
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Controller controller) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth, this.scene);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		this.controller = controller; // Initialize controller
		this.levelUIManager = new LevelUIManager(screenWidth, screenHeight, root, controller);
		initializeTimeline();
		friendlyUnits.add(user);
	}

	/**
	 * Initializes the friendly units for the level (e.g., the user plane).
	 * This method must be implemented by subclasses.
	 */

	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over and takes the appropriate actions (e.g., showing game over menu or transitioning to the next level).
	 * This method must be implemented by subclasses.
	 */

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the level-specific view for the UI.
	 * This method must be implemented by subclasses.
	 *
	 * @return the LevelView for the current level
	 */

	protected abstract LevelView instantiateLevelView();

	/**
	 * Updates the level view, including UI elements such as health bars and shields.
	 */

	/**
	 * Checks whether the game is currently paused.
	 *
	 * @return true if the game is paused; false otherwise
	 */
	protected boolean isGamePaused() {
		return isPaused;
	}


	protected void updateLevelView() {
		levelUIManager.updateLevelView(user, levelView, boss);
	}

	/**
	 * Shows the game over menu, providing options to restart the level or return to the main menu.
	 */


	protected void showGameOverMenu() {
		levelUIManager.showGameOverMenu(this::restartLevel, this::goToMainMenu);
	}
	/**
	 * Shows the win menu, providing options to restart from Level One or return to the main menu.
	 */
	protected void showWinMenu() {
		levelUIManager.showWinMenu(this::restartToLevelOne, this::goToMainMenu);
	}
	/**
	 * Handles the game over logic, stopping the timeline and displaying the game over menu.
	 */
	protected void loseGame() {
		timeline.stop(); // Stop game logic
		showGameOverMenu(); // Display the game over menu
	}

	/**
	 * Starts the level and initiates the countdown timer.
	 *
	 * @param onComplete a {@link Runnable} to execute after the countdown completes
	 */
	private void startCountdown(Runnable onComplete) {
		String levelNumber = getClass().getSimpleName().replace("Level", "");
		levelUIManager.startCountdown(onComplete, levelNumber);
	}
	/**
	 * Initializes the scene for the level, including background, UI elements, and countdown animations.
	 *
	 * @return the initialized {@link Scene} for the level
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();

		// Start the countdown before enabling gameplay
		startCountdown(() -> {
			timeline.play(); // Start the game timeline
			root.getChildren().remove(levelText); // Remove the level text
		});

		// Load a retro font
		Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 20);

		// Dynamically detect the current level number from the class name
		String levelNumber = getClass().getSimpleName().replace("Level", "");

		// Level text display
		levelText = new Text("Level " + levelNumber); // Assign to instance variable
		levelText.setFont(retroFont); // Apply retro font
		levelText.setFill(Color.WHITE);

		// Position the level text below the countdown
		double textWidth = levelText.getLayoutBounds().getWidth();
		levelText.setLayoutX((getScreenWidth() - textWidth) / 2);
		levelText.setLayoutY(getScreenHeight() / 2 + 50); // Positioned 50 pixels below the countdown

		root.getChildren().add(levelText);

		// Objective text display
		objectiveText = new Text(); // Assign to instance variable
		if ("One".equals(levelNumber)) {
			objectiveText.setText("Objective: Kill 10 enemies");
		} else if ("Two".equals(levelNumber)) {
			objectiveText.setText("Objective: Kill 20 enemies");
		}
		objectiveText.setFont(retroFont);
		objectiveText.setFill(Color.YELLOW);

		// Initially position the objective text in the center of the screen
		double initialObjectiveTextX = getScreenWidth() / 2 - objectiveText.getLayoutBounds().getWidth() / 2;
		double initialObjectiveTextY = getScreenHeight() / 2 + 100; // Slightly below the level text
		objectiveText.setLayoutX(initialObjectiveTextX);
		objectiveText.setLayoutY(initialObjectiveTextY);

		root.getChildren().add(objectiveText);

		// Fade-in animation for the objective text
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), objectiveText);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setOnFinished(event -> {
			// Once fade-in is complete, move the text to its final position
			double finalObjectiveTextX = getScreenWidth() / 2 - objectiveText.getLayoutBounds().getWidth() / 2;
			double finalObjectiveTextY = getScreenHeight() * 0.12; // Position below the health bar

			TranslateTransition moveText = new TranslateTransition(Duration.seconds(1), objectiveText);
			moveText.setToX(finalObjectiveTextX - initialObjectiveTextX);
			moveText.setToY(finalObjectiveTextY - initialObjectiveTextY);
			moveText.play();
		});
		fadeIn.play();

		// Fade-in animation for the level start
		FadeTransition levelFadeIn = new FadeTransition(Duration.seconds(1), root);
		levelFadeIn.setFromValue(0.0);
		levelFadeIn.setToValue(1.0);
		levelFadeIn.play();

		return scene;
	}


	/**
	 * Starts the game by requesting focus on the background and playing the game timeline.
	 */
	public void startGame() {
		background.requestFocus();
	}

	/**
	 * Transitions to the next level with a fade-out animation.
	 *
	 * @param levelName the name of the next level to transition to
	 */
	public void goToNextLevel(String levelName) {
		transitioningToNextLevel = true; // Start transition
		timeline.stop();
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setOnFinished(e -> {
			setChanged();
			notifyObservers(levelName);
			transitioningToNextLevel = false; // End transition
		});
		fadeOut.play();
	}
	/**
	 * Stops the game timeline.
	 */
	public void stop() {
		timeline.stop();
	}

	/**
	 * Gets the LevelView associated with this level.
	 *
	 * @return the LevelView instance
	 */
	public LevelView getLevelView() {
		return levelView;
	}


	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
		timeline.pause(); // Ensure the timeline is paused until the countdown ends
	}


	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		background.setOnKeyPressed(e -> {
			KeyCode kc = e.getCode();
			if (kc == KeyCode.UP) user.moveUp();
			if (kc == KeyCode.DOWN) user.moveDown();
			if (kc == KeyCode.LEFT) user.moveLeft(); // Add left movement
			if (kc == KeyCode.RIGHT) user.moveRight(); // Add right movement
			if (kc == KeyCode.SPACE) fireProjectile();
			if (kc == KeyCode.ESCAPE) togglePause();
		});

		background.setOnKeyReleased(e -> {
			KeyCode kc = e.getCode();
			if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) user.stopHorizontal(); // Stop horizontal movement
		});

		root.getChildren().add(background);
	}

	private void togglePause() {
		if (isPaused) {
			resumeGame();
		} else {
			pauseGame();
		}
	}
	/**
	 * Pauses the game, showing the pause menu and stopping the game timeline.
	 */
	public void pauseGame() {
		isPaused = true;
		timeline.pause();

		if (pauseMenu == null) {
			pauseMenu = new PauseMenu(
					screenWidth,
					screenHeight,
					this::resumeGame,
					this::restartLevel,
					this::goToMainMenu
			);
		}

		root.getChildren().add(pauseMenu.getRoot());
	}

	/**
	 * Resumes the game if it is paused, and removes the pause menu from the scene.
	 */
	public void resumeGame() {
		isPaused = false;
		timeline.play();

		if (pauseMenu != null) {
			root.getChildren().remove(pauseMenu.getRoot());
		}
	}

	/**
	 * Restarts the current level by stopping the timeline and reloading the level.
	 */
	public void restartLevel() {
		timeline.stop();
		// Restart the current level
		setChanged();
		notifyObservers(this.getClass().getName());
	}

	public void goToMainMenu() {
		timeline.stop();
		setChanged();
		notifyObservers("MAIN_MENU"); // Use a constant string identifier for the main menu
	}

	/**
	 * Gets the screen height.
	 *
	 * @return the screen height
	 */
	public double getScreenHeight() {
		return this.scene.getHeight();
	}




	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) { // Only add projectile if it was created
			root.getChildren().add(projectile);
			userProjectiles.add(projectile);
		}
	}


	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}


	private final Map<ActiveActorDestructible, Rectangle> boundingBoxHighlights = new HashMap<>();

	// TESTING
//	private void updateActors() {
//		friendlyUnits.forEach(plane -> {
//			plane.updateActor();
//			// Update bounding box visualization
//			Rectangle highlight = boundingBoxHighlights.computeIfAbsent(plane, p -> {
//				Rectangle rect = new Rectangle();
//				rect.setStroke(Color.RED);
//				rect.setFill(Color.TRANSPARENT);
//				root.getChildren().add(rect);
//				return rect;
//			});
//			plane.updateBoundingBoxHighlight(highlight);
//		});
//		enemyUnits.forEach(enemy -> {
//			enemy.updateActor();
//			// Update bounding box visualization
//			Rectangle highlight = boundingBoxHighlights.computeIfAbsent(enemy, e -> {
//				Rectangle rect = new Rectangle();
//				rect.setStroke(Color.RED);
//				rect.setFill(Color.TRANSPARENT);
//				root.getChildren().add(rect);
//				return rect;
//			});
//			enemy.updateBoundingBoxHighlight(highlight);
//		});
//		userProjectiles.forEach(projectile -> projectile.updateActor());
//		enemyProjectiles.forEach(projectile -> projectile.updateActor());
//	}

	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}


	//TESTING DEBUG
	// Remove bounding boxes when an actor is destroyed
//	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
//		List<ActiveActorDestructible> destroyedActors = actors.stream()
//				.filter(ActiveActorDestructible::isDestroyed)
//				.collect(Collectors.toList());
//		destroyedActors.forEach(actor -> {
//			// Remove bounding box visualization
//			Rectangle highlight = boundingBoxHighlights.remove(actor);
//			if (highlight != null) {
//				root.getChildren().remove(highlight);
//			}
//		});
//		root.getChildren().removeAll(destroyedActors);
//		actors.removeAll(destroyedActors);
//	}


	// Remove bounding box logic in removeDestroyedActors
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}


	private void handleUserProjectileCollisions() {
		List<ActiveActorDestructible> enemiesToRemove = new ArrayList<>();
		List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();

		// Handle collisions with enemy units
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (!enemy.isVisibleOnScreen(screenWidth, screenHeight)) {
				continue; // Skip enemies not visible on the screen
			}
			for (ActiveActorDestructible projectile : userProjectiles) {
				if (enemy.getAdjustedBounds().intersects(projectile.getAdjustedBounds())) {
					enemy.takeDamage();
					projectile.takeDamage();

					if (enemy.isDestroyed()) {
						user.incrementKillCount();
						System.out.println("Enemy destroyed. Total kills: " + user.getNumberOfKills());
						enemiesToRemove.add(enemy);

						// Remove bounding box visualization
						Rectangle highlight = boundingBoxHighlights.remove(enemy);
						if (highlight != null) {
							root.getChildren().remove(highlight);
						}
					}
					if (projectile.isDestroyed()) {
						projectilesToRemove.add(projectile);
					}
				}
			}
		}

		// Handle collisions with the boss
		if (boss != null) {
			for (ActiveActorDestructible projectile : userProjectiles) {
				if (boss.isShielded()) {
					if (boss.getAdjustedBounds().intersects(projectile.getAdjustedBounds())) {
						boss.takeDamage(); // Damage the shield
						projectile.takeDamage(); // Destroy the projectile
						if (projectile.isDestroyed()) {
							projectilesToRemove.add(projectile);
						}
					}
				} else if (boss.getAdjustedBounds().intersects(projectile.getAdjustedBounds())) {
					boss.takeDamage(); // Damage the boss directly
					projectile.takeDamage(); // Destroy the projectile
					if (projectile.isDestroyed()) {
						projectilesToRemove.add(projectile);
					}
				}
			}
		}

		// Handle collisions between enemy projectiles and the user plane
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (user.getAdjustedBounds().intersects(projectile.getAdjustedBounds())) {
				user.takeDamage(); // Damage the user plane
				projectile.takeDamage(); // Destroy the projectile
				if (projectile.isDestroyed()) {
					projectilesToRemove.add(projectile);
				}

				// Check if the user plane is destroyed
				if (user.isDestroyed()) {
					System.out.println("User plane destroyed. Game over.");
					loseGame();
					return; // Exit the function early if the game is over
				}
			}
		}

		// Remove destroyed enemies, projectiles, and update the game state
		root.getChildren().removeAll(enemiesToRemove);
		root.getChildren().removeAll(projectilesToRemove);
		enemyUnits.removeAll(enemiesToRemove);
		userProjectiles.removeAll(projectilesToRemove);
		enemyProjectiles.removeAll(projectilesToRemove);

		// Update the kill count display
		levelView.updateKillCountDisplay(user.getNumberOfKills());
		System.out.println("Kill count updated in UI: " + user.getNumberOfKills());
	}




	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}


	private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getAdjustedBounds().intersects(otherActor.getAdjustedBounds())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}


	private void handleEnemyPenetration() {
		List<ActiveActorDestructible> penetratedEnemies = new ArrayList<>();
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				// Mark enemy for removal without reducing player health
				penetratedEnemies.add(enemy);
			}
		}
		// Remove penetrated enemies from the game
		for (ActiveActorDestructible enemy : penetratedEnemies) {
			enemy.destroy();
		}
	}

	private void updateKillCount() {
		List<ActiveActorDestructible> destroyedEnemies = enemyUnits.stream()
				.filter(enemy -> enemy.isDestroyed() && enemy.isVisibleOnScreen(screenWidth, screenHeight))
				.collect(Collectors.toList());

		for (ActiveActorDestructible enemy : destroyedEnemies) {
			user.incrementKillCount();
			System.out.println("Enemy destroyed. Total kills: " + user.getNumberOfKills());
			enemyUnits.remove(enemy);
		}

		// Update the kill count display
		levelView.updateKillCountDisplay(user.getNumberOfKills());
		System.out.println("Kill count updated in UI: " + user.getNumberOfKills());
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop(); // Stop the game timeline
		levelUIManager.showWinMenu(this::restartToLevelOne, this::goToMainMenu); // Display win menu
		System.out.println("Boss defeated. Transitioning to win menu.");
	}


	/**
	 * Gets the user plane.
	 *
	 * @return the user plane
	 */
	public UserPlane getUser() {
		return user;
	}

	/**
	 * Gets the root group of the scene.
	 *
	 * @return the root group
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Gets the current number of enemies.
	 *
	 * @return the number of enemy units
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	/**
	 * Adds an enemy unit to the level.
	 *
	 * @param enemy the enemy unit to add
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	/**
	 * Gets the maximum Y position for enemies.
	 *
	 * @return the maximum Y position
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Gets the screen width.
	 *
	 * @return the screen width
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user is destroyed.
	 *
	 * @return true if the user is destroyed; false otherwise
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Adds an enemy projectile to the level.
	 *
	 * @param projectile the projectile to add
	 */
	protected void addEnemyProjectile(ActiveActorDestructible projectile) {
		enemyProjectiles.add(projectile);
		root.getChildren().add(projectile);
	}


	/**
	 * Resets the user's health to the specified value.
	 *
	 * @param health the new health value
	 */
	// Add a method to reset the user's health when transitioning to a new level
	protected void resetUserHealth(int health) {
		user.setHealth(health); // Reset the health
		System.out.println("User health reset to: " + user.getHealth());
	}


	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	/**
	 * Restarts the game from Level One.
	 */
	protected void restartToLevelOne() {
		timeline.stop(); // Stop the game timeline
		try {
			controller.goToLevel("com.example.demo.levels.LevelOne"); // Restart to Level 1
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
