# Tank Game

A modern recreation of the classic 1980s game **Battle City** by Namco for the NES, built from scratch using Java and Maven. This is an academic project that reimplements the beloved tank combat game with enhanced features including online multiplayer, custom maps, and power-up systems.

## Screenshots

*[Screenshots will be added here]*

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Building the Project](#building-the-project)
- [Running the Game](#running-the-game)
- [Game Modes](#game-modes)
- [Controls](#controls)
- [Power-Ups](#power-ups)
- [Online Multiplayer](#online-multiplayer)
- [Project Structure](#project-structure)

## Features

- **Classic Gameplay**: Defend your base (eagle) from waves of enemy tanks
- **Multiple Difficulty Levels**: Easy, Normal, and Hard modes with different enemy counts and score multipliers
- **Progressive Level System**: Advance through multiple maps with increasing difficulty
- **Power-Up System**: Collect various power-ups to enhance your capabilities
- **Online Multiplayer**: Play with friends over WebSocket connections
- **Map Creator**: Design and create custom battle arenas
- **Ranking System**: Track high scores and compete for the best times
- **Custom Graphics**: Retro-style sprites faithful to the original game

## Requirements

- **Java**: Version 17 or higher (Java 25 preferred)
- **Maven**: Version 3.6+ for building the project
- **Node.js**: Required for running the online multiplayer server (if using online features)

## Building the Project

### 1. Clone or Download the Project

```bash
cd tank-game
```

### 2. Build with Maven

Build the project and create the executable JAR:

```bash
mvn clean package
```

This will:
- Compile all Java source files
- Run unit tests
- Package the application with all dependencies
- Create `tank-game.jar` in the `target/` directory

### 3. Build Output

After successful compilation, you'll find:
- `target/tank-game.jar` - Executable JAR with all dependencies included
- `target/tank-game-1.0-SNAPSHOT.jar` - Standard JAR without dependencies

## Running the Game

### Single Player Mode

Run the game using the shaded JAR:

```bash
java -jar target/tank-game.jar
```

Or using Maven:

```bash
mvn exec:java
```

### Running with IDE

The main class is `com.tankgame.Main`. Configure your IDE to run this class with the project classpath.

## Game Modes

### Single Player
- Choose from three difficulty levels:
  - **Easy (0)**: 2 initial enemies per level
  - **Normal (1)**: 3 initial enemies per level
  - **Hard (2)**: 5 initial enemies per level
- Enemy count increases by 30% with each level progression
- Score multipliers: 1x (Easy), 2x (Normal), 3x (Hard)

### Online Multiplayer
- Connect to a game server for cooperative or competitive play
- Requires the Node.js server to be running (see [Online Multiplayer](#online-multiplayer))

### Map Creator
- Design custom maps with walls, obstacles, and spawn points
- Save and load custom map configurations

## Controls

### Movement
- **W** or **↑**: Move Up
- **S** or **↓**: Move Down
- **A** or **←**: Move Left
- **D** or **→**: Move Right

### Actions
- **SPACE** or **Z**: Shoot
- **ESC**: Pause/Resume game

## Power-Ups

Power-ups spawn randomly during gameplay and provide various advantages:

| Power-Up | Name | Effect |
|----------|------|--------|
| ⭐ | **Star** | Increases fire rate (reduces cooldown to 250ms) |
| 💣 | **Grenade** | Destroys all enemies on the map instantly |
| 🛡️ | **Helmet** | Grants temporary invulnerability (~11 seconds) |
| 🚧 | **Shovel** | Protects your base with steel walls |
| ❤️ | **Health Pack** | Adds one health point |
| ⏱️ | **Time Stop** | Freezes all enemies for 9 seconds ("ZA WARUDO!") |

## Online Multiplayer

### Server Setup

The game requires a Node.js WebSocket server for online features.

1. **Navigate to the server directory:**
   ```bash
   cd server
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the server:**
   ```bash
   node server.js
   ```

   The server will start on `localhost:3000` by default.

### Connecting to Online Games

1. Launch the game
2. Select "Online" from the main menu
3. Enter the server address (default: `localhost:3000`)
4. Join or create a lobby

## Project Structure

```
tank-game/
├── src/
│   ├── main/java/com/tankgame/
│   │   ├── Main.java                    # Entry point
│   │   ├── entities/                    # Game entities
│   │   │   ├── Entity.java
│   │   │   ├── collectible/            # Power-ups
│   │   │   ├── projectile/             # Bullets
│   │   │   ├── tank/                   # Player and enemy tanks
│   │   │   └── tile/                   # Map tiles and obstacles
│   │   ├── game/                       # Core game logic
│   │   │   ├── GameEngine.java
│   │   │   ├── GameGrid.java
│   │   │   ├── GameManager.java
│   │   │   ├── MainWindow.java
│   │   │   └── online/                 # Online multiplayer
│   │   ├── graphics/                   # Rendering
│   │   │   ├── Renderer.java
│   │   │   └── SpriteImport.java
│   │   ├── input/                      # Input handling
│   │   │   ├── Gamepad.java
│   │   │   └── KeyboardInput.java
│   │   ├── managers/                   # Game systems
│   │   │   ├── CollisionManager.java
│   │   │   ├── EnemyManager.java
│   │   │   ├── FontManager.java
│   │   │   ├── PowerUpManager.java
│   │   │   ├── ProjectileManager.java
│   │   │   ├── RankingManager.java
│   │   │   └── StatManager.java
│   │   ├── screens/                    # UI screens
│   │   │   ├── GameScene.java
│   │   │   ├── InstructionsScreen.java
│   │   │   ├── MapCreatorScreen.java
│   │   │   ├── OptionsScreen.java
│   │   │   ├── RankingScreen.java
│   │   │   └── StartScreen.java
│   │   ├── settings/                   # Configuration
│   │   ├── systems/                    # AI and other systems
│   │   └── utils/                      # Utility classes
│   └── test/                           # Unit tests
├── assets/                             # Game assets
│   ├── sprites/                        # Graphics
│   └── ttf/                           # Fonts
├── world/                              # Map files
│   ├── scene_00.txt to scene_05.txt
│   ├── scene_online.txt
│   └── custommap.txt
├── server/                             # Node.js multiplayer server
├── UML/                                # Project diagrams
├── pom.xml                             # Maven configuration
└── README.md                           # This file
```

## Gameplay Tips

1. **Protect Your Base**: The eagle is your most important asset. Its destruction means game over.
2. **Use Cover**: Hide behind brick walls to avoid enemy fire, but remember they can be destroyed.
3. **Collect Power-Ups**: Power-ups can turn the tide of battle, especially the Grenade and Time Stop.
4. **Manage Your Health**: Each tank (player and enemy) has health points. Plan your attacks carefully.
5. **Strategic Positioning**: Use the map layout to your advantage and control enemy spawn points.

## Technical Details

### Dependencies
- **Java WebSocket API** (javax.websocket-api 1.1)
- **Tyrus WebSocket Client** (1.17)
- **JUnit Jupiter** (5.11.0) - for testing
- **Java Swing** - for GUI

### Build Plugins
- **Maven Compiler Plugin** (3.13.0)
- **Maven Shade Plugin** (3.5.0) - for creating fat JAR
- **Maven Surefire Plugin** (3.3.0) - for running tests

### Configuration
- Target Java Version: 17
- Source Encoding: UTF-8
- Main Class: `com.tankgame.Main`

## Development

### Running Tests

```bash
mvn test
```

### Clean Build

```bash
mvn clean
```

### Package Without Tests

```bash
mvn package -DskipTests
```

## Credits

This is an academic project recreating the classic 1980s game **Battle City** by Namco for the NES. While the game mechanics and concept are inspired by the original, all code has been written from scratch. The sprite graphics are reused from the original game for authenticity.

## Troubleshooting

### Game Won't Start
- Ensure you're using Java 17 or higher: `java -version`
- Verify the JAR was built correctly: check for `target/tank-game.jar`
- Try rebuilding: `mvn clean package`

### Online Mode Connection Issues
- Ensure the Node.js server is running on `localhost:3000`
- Check firewall settings
- Verify Node.js dependencies are installed in the `server/` directory

### Graphics Not Loading
- Ensure the `assets/` directory is present in the same location as the JAR
- Check that the build process includes resources (configured in `pom.xml`)

### Performance Issues
- Try closing other applications to free up memory
- Ensure you're using a recent Java version (Java 17+)
- Check that your system meets minimum requirements

---

**Enjoy the game and defend your base!** 🎮🚀
