package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.controller.PauseMenu;
import javafx.animation.*;
import javafx.event.EventHandler;
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
	private final Timeline timeline;
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

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
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
		initializeTimeline();
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();

		// Load a retro font
		Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 20);

		// Dynamically detect the current level number from the class name
		String levelNumber = getClass().getSimpleName().replace("Level", "");

		// Level text display
		Text levelText = new Text("Level " + levelNumber);
		levelText.setFont(retroFont); // Apply retro font
		levelText.setFill(Color.WHITE);

		// Calculate text width and center it
		double textWidth = levelText.getLayoutBounds().getWidth();
		levelText.setLayoutX((getScreenWidth() - textWidth) / 2);
		levelText.setLayoutY(getScreenHeight() / 2);

		root.getChildren().add(levelText);

		// Fade-out effect after 2 seconds
		Timeline hideLevelText = new Timeline(new KeyFrame(Duration.seconds(2), e -> root.getChildren().remove(levelText)));
		hideLevelText.play();

		// Fade-in effect for level start
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();

		return scene;
	}





	public void startGame() {
		background.requestFocus();
		startCountdown(() -> timeline.play());
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

	private void startCountdown(Runnable onComplete) {
		// Load the retro font
		Font retroFont = Font.loadFont(getClass().getResourceAsStream("/com/example/demo/images/PressStart2P-Regular.ttf"), 50);

		// Dynamically detect the current level number from the class name
		String levelNumber = getClass().getSimpleName().replace("Level", "");

		Text countdownText = new Text();
		countdownText.setFont(retroFont); // Apply retro font
		countdownText.setFill(Color.WHITE);

		// Calculate text width and center it dynamically
		countdownText.setText("3");
		double textWidth = countdownText.getLayoutBounds().getWidth();
		countdownText.setLayoutX((getScreenWidth() - textWidth) / 2);
		countdownText.setLayoutY(getScreenHeight() / 2);

		root.getChildren().add(countdownText);

		Timeline countdownTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0), e -> {
					countdownText.setText("3");
					double width = countdownText.getLayoutBounds().getWidth();
					countdownText.setLayoutX((getScreenWidth() - width) / 2);
				}),
				new KeyFrame(Duration.seconds(1), e -> {
					countdownText.setText("2");
					double width = countdownText.getLayoutBounds().getWidth();
					countdownText.setLayoutX((getScreenWidth() - width) / 2);
				}),
				new KeyFrame(Duration.seconds(2), e -> {
					countdownText.setText("1");
					double width = countdownText.getLayoutBounds().getWidth();
					countdownText.setLayoutX((getScreenWidth() - width) / 2);
				}),
				new KeyFrame(Duration.seconds(3), e -> {
					// Display the correct level dynamically
					countdownText.setText("Level " + levelNumber);
					double width = countdownText.getLayoutBounds().getWidth();
					countdownText.setLayoutX((getScreenWidth() - width) / 2);
				}),
				new KeyFrame(Duration.seconds(4), e -> {
					root.getChildren().remove(countdownText);
					onComplete.run(); // Start game logic here
				})
		);

		countdownTimeline.play();
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
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
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

//	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
//		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
//				.collect(Collectors.toList());
//		root.getChildren().removeAll(destroyedActors);
//		actors.removeAll(destroyedActors);
//	}

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

	// OLD CODE
//	private void handleUserProjectileCollisions() {
//		handleCollisions(userProjectiles, enemyUnits);
//	}

	// NEW CODE
	private void handleUserProjectileCollisions() {
		List<ActiveActorDestructible> enemiesToRemove = new ArrayList<>();
		List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();

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

		// Temporarily remove enemies and projectiles from the game
		root.getChildren().removeAll(enemiesToRemove);
		root.getChildren().removeAll(projectilesToRemove);
		enemyUnits.removeAll(enemiesToRemove);
		userProjectiles.removeAll(projectilesToRemove);

		// Update the kill count display
		levelView.updateKillCountDisplay(user.getNumberOfKills());
		System.out.println("Kill count updated in UI: " + user.getNumberOfKills());
	}




	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

//	private void handleCollisions(List<ActiveActorDestructible> actors1,
//			List<ActiveActorDestructible> actors2) {
//		for (ActiveActorDestructible actor : actors2) {
//			for (ActiveActorDestructible otherActor : actors1) {
//				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
//					actor.takeDamage();
//					otherActor.takeDamage();
//				}
//			}
//		}
//	}

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

// OLD CODE
//	private void handleEnemyPenetration() {
//		for (ActiveActorDestructible enemy : enemyUnits) {
//			if (enemyHasPenetratedDefenses(enemy)) {
//				user.takeDamage();
//				enemy.destroy();
//			}
//		}
//	}

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


	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		if (levelView instanceof LevelViewLevelTwo && boss != null) { // Null check for boss
			LevelViewLevelTwo levelTwoView = (LevelViewLevelTwo) levelView;
			levelTwoView.updateBossHealthBar(boss.getHealth(), 100);
			levelTwoView.updateShieldPosition(boss.getTranslateX(), boss.getTranslateY());
			if (boss.isShielded()) {
				levelTwoView.showShield();
			} else {
				levelTwoView.hideShield();
			}
		}
		System.out.println("LevelView updated: Boss health and shield status.");
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
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}

	protected UserPlane getUser() {
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

}
