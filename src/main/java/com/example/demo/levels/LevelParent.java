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

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	protected final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	public final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private int currentNumberOfEnemies;
	private LevelView levelView;
	protected Boss boss;

	private boolean isPaused = false;
	private PauseMenu pauseMenu;

	private boolean transitioningToNextLevel = false;
	private final Controller controller;

	private LevelUIManager levelUIManager;
	private Text levelText;
	private Text objectiveText;

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

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	protected boolean isGamePaused() {
		return isPaused;
	}


	protected void updateLevelView() {
		levelUIManager.updateLevelView(user, levelView, boss);
	}
	protected void showGameOverMenu() {
		levelUIManager.showGameOverMenu(this::restartLevel, this::goToMainMenu);
	}

	protected void showWinMenu() {
		levelUIManager.showWinMenu(this::restartToLevelOne, this::goToMainMenu);
	}

	protected void loseGame() {
		timeline.stop(); // Stop game logic
		showGameOverMenu(); // Display the game over menu
	}


	private void startCountdown(Runnable onComplete) {
		String levelNumber = getClass().getSimpleName().replace("Level", "");
		levelUIManager.startCountdown(onComplete, levelNumber);
	}

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



	public void startGame() {
		background.requestFocus();
	}

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

	public void stop() {
		timeline.stop();
	}

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

	public void resumeGame() {
		isPaused = false;
		timeline.play();

		if (pauseMenu != null) {
			root.getChildren().remove(pauseMenu.getRoot());
		}
	}

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
	private void updateActors() {
		friendlyUnits.forEach(plane -> {
			plane.updateActor();
			// Update bounding box visualization
			Rectangle highlight = boundingBoxHighlights.computeIfAbsent(plane, p -> {
				Rectangle rect = new Rectangle();
				rect.setStroke(Color.RED);
				rect.setFill(Color.TRANSPARENT);
				root.getChildren().add(rect);
				return rect;
			});
			plane.updateBoundingBoxHighlight(highlight);
		});
		enemyUnits.forEach(enemy -> {
			enemy.updateActor();
			// Update bounding box visualization
			Rectangle highlight = boundingBoxHighlights.computeIfAbsent(enemy, e -> {
				Rectangle rect = new Rectangle();
				rect.setStroke(Color.RED);
				rect.setFill(Color.TRANSPARENT);
				root.getChildren().add(rect);
				return rect;
			});
			enemy.updateBoundingBoxHighlight(highlight);
		});
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}


	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}


	//TESTING DEBUG
	// Remove bounding boxes when an actor is destroyed
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		destroyedActors.forEach(actor -> {
			// Remove bounding box visualization
			Rectangle highlight = boundingBoxHighlights.remove(actor);
			if (highlight != null) {
				root.getChildren().remove(highlight);
			}
		});
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}


	// NEW CODE
//	private void handleUserProjectileCollisions() {
//		List<ActiveActorDestructible> enemiesToRemove = new ArrayList<>();
//		List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();
//
//		for (ActiveActorDestructible enemy : enemyUnits) {
//			if (!enemy.isVisibleOnScreen(screenWidth, screenHeight)) {
//				continue; // Skip enemies not visible on the screen
//			}
//			for (ActiveActorDestructible projectile : userProjectiles) {
//				if (enemy.getAdjustedBounds().intersects(projectile.getAdjustedBounds())) {
//					enemy.takeDamage();
//					projectile.takeDamage();
//
//					if (enemy.isDestroyed()) {
//						user.incrementKillCount();
//						System.out.println("Enemy destroyed. Total kills: " + user.getNumberOfKills());
//						enemiesToRemove.add(enemy);
//
//						// Remove bounding box visualization
//						Rectangle highlight = boundingBoxHighlights.remove(enemy);
//						if (highlight != null) {
//							root.getChildren().remove(highlight);
//						}
//					}
//					if (projectile.isDestroyed()) {
//						projectilesToRemove.add(projectile);
//					}
//				}
//			}
//		}
//
//		// Temporarily remove enemies and projectiles from the game
//		root.getChildren().removeAll(enemiesToRemove);
//		root.getChildren().removeAll(projectilesToRemove);
//		enemyUnits.removeAll(enemiesToRemove);
//		userProjectiles.removeAll(projectilesToRemove);
//
//		// Update the kill count display
//		levelView.updateKillCountDisplay(user.getNumberOfKills());
//		System.out.println("Kill count updated in UI: " + user.getNumberOfKills());
//	}


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



	public UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}
	protected void addEnemyProjectile(ActiveActorDestructible projectile) {
		enemyProjectiles.add(projectile);
		root.getChildren().add(projectile);
	}


	// Add a method to reset the user's health when transitioning to a new level
	protected void resetUserHealth(int health) {
		user.setHealth(health); // Reset the health
		System.out.println("User health reset to: " + user.getHealth());
	}


	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}


	protected void restartToLevelOne() {
		timeline.stop(); // Stop the game timeline
		try {
			controller.goToLevel("com.example.demo.levels.LevelOne"); // Restart to Level 1
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
