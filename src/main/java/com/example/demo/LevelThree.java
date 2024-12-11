package com.example.demo;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private Boss boss;
    private LevelViewLevelTwo levelView;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        boss = new Boss(this);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        }
        else if (boss.isDestroyed()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            if (boss == null) {
                boss = new Boss(this); // Pass the current level instance
                System.out.println("Boss initialized.");
            }
            addEnemyUnit(boss);
            System.out.println("Boss added to enemy units.");
        }
    }




    @Override
    protected void updateLevelView() {
        super.updateLevelView(); // Update hearts and other UI elements
        adjustBossPosition();
        if (boss != null) {
            levelView.updateBossHealthBar(boss.getHealth(), 100);

            // Sync shield position with the boss
            double bossX = boss.getLayoutX() + boss.getTranslateX();
            double bossY = boss.getLayoutY() + boss.getTranslateY();
            levelView.updateShieldPosition(bossX, bossY);

            // Handle shield visibility
            if (boss.isShielded()) {
                System.out.println("Shield should be visible.");
                levelView.showShield();
            } else {
                System.out.println("Shield should be hidden.");
                levelView.hideShield();
            }
        }
    }

    public void adjustBossPosition() {
        if (boss != null) {
            double bossX = getScreenWidth() * 0.9 - boss.getBoundsInLocal().getWidth() / 2; // 90% from the left edge
            double bossY = getScreenHeight() * 0.5 - boss.getBoundsInLocal().getHeight() / 2; // Centered vertically

            boss.setLayoutX(bossX);
            boss.setLayoutY(bossY);

            System.out.println("Boss position adjusted to: (" + bossX + ", " + bossY + ")");
        }
    }




//    public void updateBossShieldPosition(double bossX, double bossY) {
//        if (levelView instanceof LevelViewLevelTwo ) {
//            LevelViewLevelTwo levelTwoView = (LevelViewLevelTwo) levelView;
//            levelTwoView.updateShieldPosition(bossX, bossY);
//        }
//    }

public void updateBossShieldPosition(double bossX, double bossY) {
    levelView.updateShieldPosition(bossX, bossY);
}


    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

}
