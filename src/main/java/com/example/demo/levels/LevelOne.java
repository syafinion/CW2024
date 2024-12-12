package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.views.LevelView;
import com.example.demo.controller.Controller;

public class LevelOne extends LevelParent {

	protected static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/levelonebg.gif";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	protected static final double ENEMY_SPAWN_PROBABILITY = .20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelOne(double screenHeight, double screenWidth, Controller controller) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, controller); // Pass the controller
	}




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




	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

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


	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, false); // Pass false for isLevelThree
	}


	protected boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

}
