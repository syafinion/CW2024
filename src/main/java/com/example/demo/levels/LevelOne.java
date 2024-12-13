package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.views.LevelView;
import com.example.demo.controller.Controller;

/**
 * Represents the first level of the game.
 * <p>
 * LevelOne introduces basic gameplay mechanics such as spawning enemies,
 * managing the player's health, and transitioning to the next level after
 * meeting specific criteria.
 * </p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Spawns enemies at random positions with a specified probability.</li>
 *   <li>Tracks the player's progress, such as health and number of kills.</li>
 *   <li>Transitions to LevelTwo upon meeting the kill target.</li>
 * </ul>
 */
public class LevelOne extends LevelParent {

	/** Background image for Level One. */
	protected static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/levelonebg.gif";

	/** The fully qualified name of the next level (LevelTwo). */
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";

	/** Total number of enemies to spawn in this level. */
	private static final int TOTAL_ENEMIES = 5;

	/** Number of kills required to advance to the next level. */
	private static final int KILLS_TO_ADVANCE = 10;

	/** Probability of spawning an enemy during each spawn attempt. */
	protected static final double ENEMY_SPAWN_PROBABILITY = 0.20;

	/** Initial health of the player in this level. */
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * Constructs a new LevelOne instance.
	 *
	 * @param screenHeight the height of the game screen
	 * @param screenWidth  the width of the game screen
	 * @param controller   the controller managing game logic and transitions
	 */
	public LevelOne(double screenHeight, double screenWidth, Controller controller) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, controller);
	}

	/**
	 * Checks if the game is over and performs the appropriate actions.
	 * <ul>
	 *   <li>If the player is destroyed, transitions to the game over menu.</li>
	 *   <li>If the player reaches the kill target, transitions to the next level.</li>
	 *   <li>Otherwise, logs the current player status (health and kills).</li>
	 * </ul>
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			System.out.println("Player is destroyed. Game over.");
			loseGame();
		} else if (userHasReachedKillTarget()) {
			System.out.println("Kill target reached. Transitioning to next level.");
			goToNextLevel(NEXT_LEVEL);
		} else {
			System.out.println("Player health: " + getUser().getHealth() +
					", Kills: " + getUser().getNumberOfKills() +
					", Required kills to advance: " + KILLS_TO_ADVANCE);
		}
	}

	/**
	 * Initializes the player's unit (friendly units) at the start of the level.
	 * Adds the player character to the scene's root node.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Spawns enemy units during gameplay.
	 * <p>
	 * Enemies are spawned at random positions along the right edge of the screen
	 * and added to the game's root node. Ensures no overlap with existing enemies.
	 * </p>
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

							return distanceX < 100 && distanceY < 50; // Minimum distance of 100 horizontally, 50 vertically
						});

				if (positionValid) {
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
	 * Instantiates the LevelView for LevelOne.
	 * <p>
	 * The LevelView displays the player's health and other level-specific UI elements.
	 * </p>
	 *
	 * @return a new instance of LevelView customized for LevelOne
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, false); // Pass false for isLevelThree
	}

	/**
	 * Checks if the player has reached the kill target for the level.
	 *
	 * @return {@code true} if the player has achieved the required number of kills;
	 *         {@code false} otherwise
	 */
	protected boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}
}
