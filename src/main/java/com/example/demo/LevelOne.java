package com.example.demo;

public class LevelOne extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
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
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				EnemyPlane newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition, getUser()); // Pass UserPlane
				addEnemyUnit(newEnemy);
				System.out.println("Spawned enemy at Y: " + newEnemyInitialYPosition);

				// Add homing projectile fired by enemy
				ActiveActorDestructible homingProjectile = newEnemy.fireProjectile();
				if (homingProjectile != null) {
					addEnemyProjectile(homingProjectile);
				}
			}
		}
	}








	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

}
