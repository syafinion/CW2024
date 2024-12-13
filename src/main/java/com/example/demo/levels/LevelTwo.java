package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.views.LevelView;
import com.example.demo.controller.Controller;

/**
 * Represents Level Two of the game.
 * In this level, the player must defeat a specific number of enemies to advance.
 */
public class LevelTwo extends LevelParent {

	protected static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/leveltwobg.gif";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";

	private static final int TOTAL_ENEMIES = 8;
	private static final int KILLS_TO_ADVANCE = 20;
	protected static final double ENEMY_SPAWN_PROBABILITY = 0.30;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * Constructs a LevelTwo instance.
	 *
	 * @param screenHeight the height of the screen
	 * @param screenWidth  the width of the screen
	 * @param controller   the game controller
	 */
	public LevelTwo(double screenHeight, double screenWidth, Controller controller) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, controller); // Pass the controller
		resetUserHealth(PLAYER_INITIAL_HEALTH); // Ensure health is reset
		System.out.println("User health initialized to: " + PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Checks if the game is over or if the player has met the requirements to advance.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			System.out.println("Player is destroyed. Game over.");
			loseGame();
		} else if (userHasReachedKillTarget()) {
			System.out.println("Kill target reached for Level Two. Transitioning to next level.");
			goToNextLevel(NEXT_LEVEL);
		} else {
			System.out.println("Player health: " + getUser().getHealth() +
					", Kills: " + getUser().getNumberOfKills() +
					", Required kills to advance: " + KILLS_TO_ADVANCE);
		}
	}

	/**
	 * Initializes the player's units for this level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		resetUserHealth(PLAYER_INITIAL_HEALTH); // Ensure health is reset first
		getRoot().getChildren().add(getUser()); // Add the user to the scene
	}


	/**
	 * Spawns enemy units in the level.
	 */
	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		System.out.println("Current number of enemies: " + currentNumberOfEnemies);

		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialXPosition = getScreenWidth(); // Spawning on the right edge
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();

				// Ensure no overlap with existing enemies
				boolean positionValid = enemyUnits.stream()
						.noneMatch(enemy -> {
							double existingX = enemy.getTranslateX();
							double existingY = enemy.getTranslateY();
							double distanceX = Math.abs(existingX - newEnemyInitialXPosition);
							double distanceY = Math.abs(existingY - newEnemyInitialYPosition);

							return distanceX < 100 && distanceY < 50; // Ensure minimum distance of 100 horizontally and 50 vertically
						});

				if (positionValid) {
					// Pass the root group to the EnemyPlane constructor
					EnemyPlane newEnemy = new EnemyPlane(newEnemyInitialXPosition, newEnemyInitialYPosition, getUser(), getRoot());
					addEnemyUnit(newEnemy);
					System.out.println("Spawned enemy at X: " + newEnemyInitialXPosition + ", Y: " + newEnemyInitialYPosition);

					// Optionally add a projectile for the enemy
					ActiveActorDestructible homingProjectile = newEnemy.fireProjectile();
					if (homingProjectile != null) {
						addEnemyProjectile(homingProjectile);
					}
				} else {
					i--; // Retry this spawn if position is invalid
				}
			}
		}
	}

	/**
	 * Instantiates the level view for Level Two.
	 *
	 * @return the LevelView instance
	 */
	@Override
	protected LevelView instantiateLevelView() {
		// Pass `false` since LevelTwo is not LevelThree
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, false);
	}

	/**
	 * Checks if the player has reached the required number of kills to advance.
	 *
	 * @return true if the player has reached the kill target, false otherwise
	 */
	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}
}
