# Online Game System Architecture

## Overview
The online game system is designed to mirror the single-player architecture while adding network capabilities via WebSocket.

## Core Components

### 1. WebSocketClient (`game/online/WebSocketClient.java`)
- **Purpose**: Manages WebSocket connection and message parsing
- **Responsibilities**:
  - Connect/disconnect from WebSocket server
  - Parse incoming JSON messages
  - Send formatted JSON messages
  - Message types handled:
    - `lobbyCreated` - Fired when lobby is created
    - `lobbyJoined` - Fired when successfully joined a lobby
    - `ready` - Fired when both players are ready
    - `gameStarted` - Fired when game begins
    - `enemyInteraction` - Received enemy actions (moveUp, moveDown, moveLeft, moveRight, shoot)
    - `error` - Error messages from server

### 2. LobbyManager (`game/online/LobbyManager.java`)
- **Purpose**: Handles lobby creation and joining (Pre-game screen)
- **Used by**: OnlineLobbyScreen
- **Features**:
  - Create a new lobby
  - Join existing lobby by ID
  - Display lobby status

### 3. OnlineLobbyScreen (`screens/OnlineLobbyScreen.java`)
- **Purpose**: UI for lobby management
- **Features**:
  - "Create Lobby" button - Creates new multiplayer session
  - "Join Lobby" button with ID input - Joins existing session
  - Displays lobby info: ID, player count
  - Shows game state: "Waiting for players...", "READY!", etc.
  - Transitions to online game when `gameStarted` message received

## Game Flow

```
1. Player launches game
   ↓
2. OnlineLobbyScreen displayed
   ├─ Option A: Click "Create Lobby" → sends createLobby to server → receives lobbyCreated
   └─ Option B: Enter ID and "Join Lobby" → sends joinLobby to server → receives lobbyJoined
   ↓
3. Both players joined → Server broadcasts "ready"
   ↓
4. Creator (Player A) clicks "Start Game" → sends startLobby
   ↓
5. Server broadcasts "gameStarted" to both players
   ↓
6. OnlineGameScene displayed (TO BE IMPLEMENTED)
   ├─ Player input → sends interaction messages
   └─ Enemy actions received → gameManager handles them
```

## Future Implementation: OnlineGameScene

When both players are ready, transition to OnlineGameScene:

```java
// Architecture (similar to GameScene):
OnlineGameScene
├─ GameGrid (same as single-player)
├─ Player (same as single-player)
├─ Enemy opponent (single enemy controlled by remote player)
├─ Renderer (same as single-player)
└─ OnlineGameManager (to be implemented)
   ├─ InputSystem - sends player actions to server
   ├─ MovementSystem (same)
   ├─ ShootingSystem (same)
   ├─ ProjectileSystem (same)
   ├─ CollisionManager (same)
   └─ WebSocketClient integration
       ├─ Receive enemyInteraction messages
       └─ Update opponent tank based on actions
```

## Message Protocol Reference

### Sending (Client → Server)
```json
// Create Lobby
{"type":"createLobby"}

// Join Lobby
{"type":"joinLobby","payload":{"id":"0421"}}

// Start Game (creator only)
{"type":"startLobby","payload":{"id":"0421"}}

// Send Action
{"type":"interaction","payload":{"action":"moveUp"}}
// Valid actions: moveUp, moveDown, moveLeft, moveRight, shoot
```

### Receiving (Server → Client)
```json
// Lobby created
{"type":"lobbyCreated","payload":{"id":"0421","size":1,"full":false,"state":"waiting"}}

// Lobby joined
{"type":"lobbyJoined","payload":{"id":"0421","size":2,"full":true,"state":"waiting"}}

// Both players ready
{"type":"ready"}

// Game started
{"type":"gameStarted","payload":{"state":"running"}}

// Enemy action received
{"type":"enemyInteraction","payload":{"action":"moveUp"}}

// Error
{"type":"error","message":"Lobby is full"}
```

## Current Status
- ✅ WebSocketClient: Fully implemented
- ✅ LobbyManager: Works with WebSocketClient
- ✅ OnlineLobbyScreen: UI complete, handles lobby lifecycle
- ✅ OnlineGameScene: Fully implemented with multiplayer rendering
- ✅ OnlineGameEngine: Game loop with input/output handling
- ✅ OnlineGridLoader: Custom map loader for online maps with 'M' spawn points
- ✅ OnlineOpponentManager: Handles single opponent tank controlled by server
- ✅ OnlinePlayerInputHandler: Integrates keyboard input with WebSocket messages

