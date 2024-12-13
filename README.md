# Coursework Project Documentation

## GitHub Repository
[Link to GitHub Repository](https://github.com/syafinion/CW2024)

---

## Compilation Instructions
1. Clone the repository:
git clone https://github.com/syafinion/CW2024.git
2. Open the project in an IDE that supports Java (e.g., IntelliJ IDEA or Eclipse).
3. Ensure Maven is installed and the dependencies are properly configured in the `pom.xml` file.
4. Build the project: mvn clean install
5. Run the application: mvn javafx:run
6. Java version: Ensure you have **Java 17** or later installed.

---

## Implemented and Working Properly
- **Main Menu**: Fully functional main menu, including "Start Game", "Settings", and "Exit". Features dynamic resolution adjustments, fullscreen toggling, and independent volume controls for music and sound effects.
- **Level Progression**: Smooth transitions between levels with distinct gameplay logic for Level One, Two, and Three, including boss-specific mechanics in the final level.
- **Enemy Behavior**: Enemies exhibit predefined behaviors, attack patterns, and collision detection. Improved logic prevents enemies from firing when the player's plane is directly beneath them or has passed their position.
- **Projectile System**: Player and enemy projectiles function correctly with collision detection, lifespan constraints, and dynamic homing behavior for specific projectiles.
- **Boss Behavior**: Fully functional boss system, including shield activation/deactivation, projectile firing, health bar updates, and smooth movement patterns. Shield follows the boss dynamically during gameplay.
- **Game Over and Victory Screens**: Properly displayed based on win/lose conditions, with visually enhanced elements such as animations and centralized layouts.
- **Settings Panel**: Includes dynamic resolution settings, volume control, fullscreen toggling, and layout adjustments to maintain UI alignment.
- **Health and Shield Visuals**: Heart icons display player health dynamically. Boss health and shield bars update in real-time, with animations for smooth transitions.
- **Pause Menu**: A retro-style pause menu allows players to resume, restart, or return to the main menu seamlessly.
- **Background Music and Sound Effects**: Fully integrated sound system with global volume controls and adjustable settings applied across all game components.

---

## Implemented but Not Working Properly
- **Homing Projectiles**: The homing logic for `BossProjectile` occasionally fails at short distances or when tracking the player's plane in rapid succession.
- **Shield Behavior**: Rare cases where the boss shield deactivates prematurely or reactivates unexpectedly during its cooldown period.
- **Explosion Effect on Damage**: Enemy planes sometimes fail to display the temporary explosion effect when hit, likely due to timing inconsistencies in animation transitions.


---

## Features Not Implemented
- **Multiplayer Mode**: A feature to allow multiple players to join the game was planned but not implemented due to time constraints.
- **Achievements System**: Tracking and displaying achievements for the player was left out.
- **Dynamic Difficulty Adjustment**: Scaling difficulty based on player performance was planned but deprioritized.
- **Advanced Shield Animations**: While the shield follows the boss correctly, planned animations for shield activation and deactivation were not fully implemented.

---

## New Java Classes
The following Java classes were introduced:
1. **`LevelUIManager.java`**:
   - Purpose: Manages the UI components for each game level, such as health bars, boss shields, and game state menus (e.g., Game Over, Victory). Provides methods to update UI elements dynamically based on the game's state.
   - Location: `src/main/java/com/example/demo/views/`

2. **`LevelThree.java`**:
   - **Purpose**: Implements the third level of the game, featuring a boss fight. Handles boss initialization, updates, and interactions with the player. Includes visual effects, such as a fade-in animation for level objectives, and manages the boss's health bar and shield status.
   - **Key Features**:
     - Adds a boss enemy with dynamic shield and health management.
     - Displays an objective text with a fade-in effect.
     - Adjusts the boss's position dynamically and synchronizes shield visibility.
     - Tracks win/lose conditions specific to the boss fight.
   - **Location**: `src/main/java/com/example/demo/levels/`


3. **`PauseMenu.java`**:
   - **Purpose**: Provides an in-game menu to pause gameplay and access various options.
   - **Key Features**:
     - Displays a semi-transparent background overlay for visual clarity during pause.
     - Offers options to resume the game, restart the current level, or return to the main menu.
     - Utilizes retro-style visuals and animations for a cohesive game aesthetic.

4. **`MainMenu.java`**:
   - **Purpose**: Serves as the primary entry point for the game, allowing users to start the game, adjust settings, or exit.
   - **Key Features**:
     - Displays a visually appealing retro-style background and animated title screen.
     - Includes options for starting the game, accessing a dynamic settings menu, or exiting the game.
     - Features a settings menu with adjustable resolution, fullscreen mode toggle, and independent volume controls for music and gunshot sounds.
     - Automatically adjusts layouts and UI elements based on screen resolution or fullscreen changes.


## Unit Tests

1. **`BossTest.java`**:
   - **Purpose**: Tests the functionality of the `Boss` class, including shield activation, movement patterns, and projectile firing.
   - **Details**:
     - Validates the boss's initial health and ensures the shield activates correctly when conditions are met.
     - Tests movement patterns to confirm the boss remains within defined boundaries.
     - Ensures damage mechanics are accurate, reducing health or shield health appropriately based on the shield state.
     - Confirms projectiles fired by the boss have the correct behavior, including tracking the player's plane and adhering to lifespan constraints.
     - Validates shield deactivation after a specified duration or health depletion.

2. **`MainMenuTest.java`**:
   - **Purpose**: Validates the behavior of the `MainMenu` class, ensuring menu navigation and interactions work correctly.
   - **Details**:
     - Tests the creation of the main menu scene and ensures that all UI elements, including background and buttons, are displayed correctly.
     - Validates resolution changes and fullscreen toggling functionality without errors.
     - Ensures the "Settings" menu adjusts resolution dynamically and updates UI layouts as expected.
     - Verifies visual effects, such as glow effects on text, are applied correctly and match specified colors.

3. **`LevelUIManagerTest.java`**:
   - **Purpose**: Ensures that the `LevelUIManager` updates health bars, displays shields, and handles game state transitions accurately.
   - **Details**:
     - Confirms the dynamic removal of hearts based on the player's remaining health.
     - Tests the update of boss health and shield health bars to reflect current status.
     - Validates the correct positioning and visibility of shields relative to the boss.
     - Tests game state transitions, ensuring game-over and victory menus are displayed when conditions are met.
     - Verifies countdown timers are added to the scene and removed after completion, triggering the correct actions.

4. **`LevelViewTest.java`**:
   - **Purpose**: Tests the rendering and update logic for level views, including interactions with the player and enemies.
   - **Details**:
     - Validates kill count display updates correctly.
     - Confirms the smooth appearance of visual elements such as health bars and shields.
     - Ensures dynamic adjustments for different screen resolutions.

5. **`LevelOneTest.java`, `LevelTwoTest.java`, `LevelThreeTest.java`**:
   - **Purpose**: Verify specific logic and gameplay features unique to each level.
   - **Details**:
     - Level One:
       - Confirms enemy spawn probabilities and kill targets.
       - Validates player health initialization.
     - Level Two:
       - Tests higher enemy spawn probabilities and increased kill targets.
       - Ensures gameplay transitions to Level Three when conditions are met.
     - Level Three:
       - Verifies boss-specific mechanics such as shield activation and health bar updates.
       - Ensures game-over and victory conditions are correctly triggered.

6. **`LevelParentTest.java`**:
   - **Purpose**: Ensures that shared functionality across all levels is implemented correctly.
   - **Details**:
     - Tests the initialization of common elements such as user planes, timelines, and scenes.
     - Validates the ability to add, remove, and update enemies dynamically.
     - Confirms that game-over and level-transition logic is correctly handled.

---

### Highlighted Tests

- **`LevelViewLevelTwoTest.java`**:
   - Verifies the correct initialization and update of the boss and shield health bars.
   - Tests the visibility and positioning of shields relative to the boss.
   - Confirms that health bar widths are dynamically updated based on current health percentages.

- **`LevelOneTest.java`**:
   - Ensures enemy spawn probabilities are correctly implemented.
   - Validates kill target progression logic, ensuring the game transitions to the next level after achieving 10 kills.

- **`LevelTwoTest.java`**:
   - Tests higher difficulty settings such as increased enemy spawn rates and stricter kill targets.
   - Validates level transition to Level Three.

- **`LevelThreeTest.java`**:
   - Verifies boss initialization and interactions.
   - Tests the display and functionality of boss health and shield bars.
   - Confirms game-over and victory scenarios are correctly triggered.

- **`LevelParentTest.java`**:
   - Ensures all shared logic and assets between levels are correctly initialized and maintained.
   - Validates the addition and removal of enemies and projectiles.


---

## Modified Java Classes
The following Java classes were modified, along with reasons for the changes:

1. **`Boss.java`**:
   - **Purpose**: Enhanced to support advanced boss behaviors such as shield activation, projectile firing, and improved movement patterns.
   - **Key Changes**:
     - Added logic for shield activation and cooldown management.
     - Integrated projectile firing mechanism with homing projectiles.
     - Enhanced movement patterns to handle edge cases.

2. **`BossProjectile.java`**:
   - **Purpose**: Added homing behavior for boss projectiles.
   - **Key Changes**:
     - Projectiles now track the player's position for a limited duration before moving in a straight line.
     - Added lifespan constraints to destroy projectiles after a certain time.

3. **`EnemyPlane.java`**:
   - **Purpose**: Represents enemy planes in the game, with movement and attack logic tailored to challenge the player.
   - **Key Changes**:
     - Added logic to ensure the enemy plane does not fire projectiles when the player's plane is directly underneath (`isUserPlaneUnderneath`).
     - Introduced a `hasPassedPlayer` flag to prevent enemy planes from shooting after they have moved past the player's position.
     - Enhanced damage visualization by adding a temporary explosion effect when the enemy plane is hit (`showDamageEffect`).
     - Included logic for removing the explosion effect from the scene after a brief display using a `PauseTransition`.
     - Improved collision and attack mechanics by introducing offsets for projectile positions relative to the plane's dimensions.

4. **`EnemyProjectile.java`**:
   - **Purpose**: Represents the enemy's projectiles, with functionality to home towards the player's position and dynamically adjust behavior based on proximity.
   - **Key Changes**:
     - **Homing Behavior**: Introduced logic to calculate a trajectory that adjusts dynamically towards the player's position if within a specified range (`HOMING_DISTANCE`).
     - **Angle Calculation**: Implemented `calculateAngleForStraightLine` to set a straight-line trajectory once homing stops.
     - **Lifespan Constraints**: Added `MAX_LIFESPAN` to ensure projectiles are destroyed after a predefined number of frames to optimize game performance.
     - **Collision and Range Logic**: Integrated checks to prevent the projectile from homing if the player is too close or out of the defined range.
     - **Screen Bounds Validation**: Added `isOffScreen` to destroy projectiles that move outside the visible game area.

5. **`FighterPlane.java`**:
   - **Purpose**: Serves as an abstract base class for all fighter planes in the game, including both enemy and player planes. Provides common functionality such as firing projectiles and handling damage.
   - **Key Changes**:
      - **Damage Handling**: Introduced `showHitEffect` to visually indicate when the plane is hit using a flash animation.
      - **Health Management**: Added logic to decrement health and destroy the plane when health reaches zero (`healthAtZero`).
      - **Projectile Position Calculation**: Added methods `getProjectileXPosition` and `getProjectileYPosition` to calculate the precise firing position for projectiles relative to the plane's current position.
      - **Enhanced Readability**: Refactored code for better modularity and maintainability by encapsulating common behaviors.

6. **`UserPlane.java`**:
   - **Purpose**: Enhanced to improve player interaction and game mechanics, including movement, firing, and health updates.
   - **Key Changes**:
      - **Health Management**: Added a `setHealth` method to dynamically adjust health during gameplay.
      - **Volume Control**: Introduced `setGunshotVolume` to allow the player to adjust the gunshot sound effect volume.
      - **Movement Enhancements**: 
        - Added horizontal movement capabilities (`moveLeft`, `moveRight`, `stopHorizontal`) in addition to vertical movement (`moveUp`, `moveDown`, `stop`).
        - Restricted movement to ensure the player remains within screen bounds using precise calculations for edges.
      - **Projectile Firing**: Implemented a firing cooldown mechanism (`FIRE_COOLDOWN_MILLIS`) to limit the rate of fire.
      - **Sound Integration**: Integrated a gunshot sound effect with volume control.
      - **Kill Tracking**: Added `getNumberOfKills` and `incrementKillCount` methods to track the player's performance.
      - **Improved Collision Safety**: Ensured proper collision checks for dynamic gameplay scenarios.

7. **`UserProjectile.java`**:
   - **Purpose**: Simplified implementation aligned with the enhanced projectile system for improved functionality.
   - **Key Changes**:
      - **Collision Safety**: Refined logic for detecting hits on shields and specific enemy types.
      - **Lifespan Constraints**: Ensured projectiles travel a limited distance to prevent lingering or off-screen issues.
      - **Streamlined Movement**: Optimized horizontal movement logic to maintain consistent behavior.

8. **`Controller.java`**:
   - **Purpose**: Centralized game state management, user input handling, and coordination of levels and menus.
   - **Key Changes**:
     - **Background Music**: Added methods to play (`playBackgroundMusic`) and stop (`stopMusic`) background music, with adjustable volume settings.
     - **Volume Control**: Introduced global and pending gunshot volume management through `setVolume`, `setPendingGunshotVolume`, and `applyPendingGunshotVolume` methods.
     - **Level Navigation**: Implemented `goToLevel` and `startLevel` methods to transition seamlessly between levels and the main menu.
     - **Level Observability**: Ensured levels notify the controller of transitions using observers.
     - **User Interaction**: Provided a method to retrieve and interact with the `UserPlane` for dynamic updates, including applying pending gunshot volume.
     - **Game Window Management**: Adjusted the application window to fullscreen or windowed mode based on user preferences.
     - **Main Menu**: Added `showMainMenu` for transitioning back to the main menu.
     - **Debugging Improvements**: Added logging for error scenarios, such as null levels or user planes.

9. **`Main.java`**:
   - **Purpose**: Entry point for the application, initializing the game window and controller.
   - **Key Changes**:
     - **Fullscreen Default**: Configured the application to start in fullscreen mode by default.
     - **Game Launch**: Integrated the `Controller` class to handle the main game logic and menu initialization.
     - **Title and Resizing**: Set the application title and disabled resizing for consistent window dimensions.

10. **`LevelOne.java`**:
    - **Purpose**: Implements the logic for the first level of the game, including enemy spawning and level progression.
    - **Key Changes**:
      - Added `checkIfGameOver` logic to transition to the next level (`LevelTwo`) when kill target is reached or end the game if the player is destroyed.
      - Introduced enhanced enemy spawning with positional validation to avoid overlap.
      - Defined level-specific properties, such as background image, player initial health, and spawn probabilities.

11. **`LevelParent.java`**:
   - **Purpose**: Serves as a base class for all game levels, providing shared logic for managing enemies, projectiles, user interactions, and game states.
   - **Key Changes**:
     - Introduced `levelUIManager` to handle UI updates, including boss health bars and shields.
     - Added `checkIfGameOver` and `goToNextLevel` methods for level transitions and game-over handling.
     - Enhanced collision handling logic for both user and enemy projectiles.
     - Added support for boss interactions, such as shield visibility and positioning updates.
     - Implemented methods to restart levels, pause/resume gameplay, and display menus for game over or victory.

12. **`LevelTwo.java`**:
    - **Purpose**: Implements the logic for the second level of the game, building on the mechanics introduced in `LevelOne`.
    - **Key Changes**:
      - Enhanced enemy spawning logic with increased spawn probability and more enemies compared to `LevelOne`.
      - Integrated logic to transition to the next level (`LevelThree`) upon meeting the kill target.
      - Ensured user health resets at the start of the level for continuity.
      - Defined level-specific properties, such as enemy spawn probabilities and kill target thresholds.

13. **`GameOverImage.java`**:
   - **Purpose**: Enhances the visual representation of the game-over screen.
   - **Key Changes**:
     - Added a smaller, centered "Game Over" image with a `.gif` animation for dynamic visuals.
     - Positioned the image above the game-over menu for better layout alignment.
     - Ensured the image scales proportionally to maintain visual clarity.

14. **`HeartDisplay.java`**:
   - **Purpose**: Provides a visual representation of the player's health using heart icons.
   - **Key Changes**:
     - Dynamically updates the heart icons to reflect the player's current health status.
     - Implemented an `HBox` container for efficient layout and rendering of heart images.
     - Introduced methods to remove hearts as the player takes damage.
     - Adjusted the size and positioning of the heart images to fit within the game's UI seamlessly.

15. **`LevelView.java`**:
   - **Purpose**: Enhanced to display additional game statistics and improve visual fidelity.
   - **Key Changes**:
     - Implemented support for dynamically displaying player health using heart icons.
     - Added a kill count display, with animations for updates.
     - Integrated methods to adjust UI element positions for different screen resolutions.
     - Enhanced the `Game Over` screen with centralized image positioning and fade effects.

16. **`LevelViewLevelTwo.java`**:
   - **Purpose**: Specialized for managing Level Two-specific UI elements and interactions.
   - **Key Changes**:
     - Introduced a boss health bar to visually represent boss damage during gameplay.
     - Added logic for displaying a shield around the boss, including positioning updates and visual effects.
     - Included methods to handle dynamic UI updates for shield and health bar positions.
     - Ensured seamless integration of additional UI elements like the shield health bar and animations.

17. **`ShieldImage.java`**:
   - **Purpose**: Introduced to visually represent the boss's shield during gameplay.
   - **Key Changes**:
     - Added methods to toggle shield visibility dynamically during boss battles.
     - Implemented error handling for missing or incorrect image file paths.
     - Optimized shield dimensions and positioning relative to the boss plane for better alignment.

18. **`WinImage.java`**:
   - **Purpose**: Added a celebratory victory screen for when the player wins.
   - **Key Changes**:
     - Designed to display a "You Win" image centered on the screen with proper scaling.
     - Included methods to show and hide the image dynamically based on game events.
     - Positioned the victory image above menu buttons for a cohesive layout.


19. **`Module-info.java`**:
    - **Purpose**: Updated to include new modules and dependencies.
    - **Key Changes**:
      - Added JavaFX module configurations.
      - Updated exports for newly introduced classes and packages.


---

## Unexpected Problems

1. **JavaFX Threading**:
   - Encountered threading issues when testing with `Platform.runLater`. This occurred because JavaFX requires UI updates to run on its designated application thread. To address this, JavaFX was initialized within the `@BeforeAll` method in tests, ensuring all JavaFX components were properly set up before tests ran.

2. **Homing Projectiles**:
   - Implementing consistent homing behavior proved challenging, particularly in edge cases. Sometimes, projectiles failed to detect the user jet or followed it indefinitely, making dodging impossible. To address this:
     - A condition was added to check the distance between the projectile and the jet. If the jet passed the projectile, the homing logic would stop.
     - Tracking was based on the jet's current position. While these changes improved the behavior, further refinement is still needed to make it fully consistent and balanced.

3. **Shield Image**:
   - Initially, the shield image was difficult to display because its functionality was not part of the original design. After adding the feature:
     - The shield sometimes failed to appear. This was resolved by adding conditions to control when the shield should be displayed.
     - The shield would occasionally appear off-screen or fail to follow the boss. This was addressed by dynamically updating the shield's position relative to the boss.
     - The shield now appears when the boss's health drops below 75%, with a certain probability, and correctly follows the boss's movement.

4. **Resolution Adjustment**:
   - Changing the screen resolution caused UI elements to shift incorrectly, resulting in misaligned components. This was resolved by implementing dynamic layout adjustment functions that reposition UI elements based on the new resolution.

5. **Sound and Sound Settings**:
   - The sound settings were not being detected throughout the game, leading to inconsistencies in volume control. This issue was resolved by updating the `Controller` to manage and apply sound settings globally across all game levels and menus.

6. **Memory Management**:
   - Memory management issues arose when transitioning between levels. Previous level elements would continue to run, causing unexpected behavior. This was resolved by stopping the timeline and ensuring all ongoing processes from the previous level were terminated before transitioning.

7. **Boss Health Bar**:
   - The boss health bar was initially difficult to display. Sometimes it failed to appear, or it displayed off-screen. Debugging was used extensively to trace and fix these errors, ensuring the health bar appears correctly and updates dynamically during gameplay.

8. **Gameplay Balancing**:
   - Balancing the gameplay presented ongoing challenges. The speed of the user plane had to be adjusted to match the updated missile tracking logic. The game could sometimes feel too hard, too easy, or too slow. This was addressed through iterative testing and adjustments to achieve a better balance.

9. **Shield Health Bar**:
   - Similar to the boss health bar, the shield health bar failed to display or update correctly in initial implementations. This was resolved by following the methods used for the boss health bar and adapting them for the shield health bar.

10. **Countdown Timer**:
    - The countdown timer did not display properly, and the game would start even before the timer had finished. This was fixed by implementing the timer in `LevelParent` and ensuring it was added to the root layout, preventing interference from other functions.

By addressing these unexpected issues, the game achieved a stable and functional state, though further refinement could improve some aspects.


---

## Summary
This project introduced several new features and improved upon the existing codebase to ensure scalability and maintainability. While some features remain incomplete, the implemented functionalities meet the primary requirements of the coursework. Additional improvements can be made in future iterations to address the unresolved issues.
