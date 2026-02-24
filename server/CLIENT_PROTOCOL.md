# WebSocket Protocol - Client Documentation

## Connection
```
ws://localhost:8080
```

---

## Message Protocol

All messages are JSON with `type` and optional `payload`.

---

## 1. Create Lobby

**Client → Server**
```json
{
  "type": "createLobby"
}
```

**Server → Client (Response)**
```json
{
  "type": "lobbyCreated",
  "payload": {
    "id": "0421",
    "size": 1,
    "full": false,
    "state": "waiting"
  }
}
```

---

## 2. List Available Lobbies

**Client → Server**
```json
{
  "type": "listLobbies"
}
```

**Server → Client (Response)**
```json
{
  "type": "lobbies",
  "payload": [
    {
      "id": "0421",
      "size": 1,
      "full": false,
      "state": "waiting"
    },
    {
      "id": "8735",
      "size": 1,
      "full": false,
      "state": "waiting"
    }
  ]
}
```

---

## 3. Join Lobby

**Client → Server**
```json
{
  "type": "joinLobby",
  "payload": {
    "id": "0421"
  }
}
```

**Server → Client (Success)**
```json
{
  "type": "lobbyJoined",
  "payload": {
    "id": "0421",
    "size": 2,
    "full": true,
    "state": "waiting"
  }
}
```

**Server → Client (Error)**
```json
{
  "type": "error",
  "message": "Lobby is full"
}
```

Or:
```json
{
  "type": "error",
  "message": "Lobby not found"
}
```

---

## 4. Ready Signal (Broadcast)

When a second player joins a lobby, the server broadcasts this to both players:

**Server → Both Clients (Broadcast)**
```json
{
  "type": "ready"
}
```

---

## 5. Creator Starts Lobby

Only the creator (first player) can start the game.

**Client → Server (Creator only)**
```json
{
  "type": "startLobby",
  "payload": {
    "id": "0421"
  }
}
```

**Server → Both Clients (Broadcast on Success)**
```json
{
  "type": "gameStarted",
  "payload": {
    "state": "running"
  }
}
```

**Server → Client (Error - not creator)**
```json
{
  "type": "error",
  "message": "Only creator can start lobby"
}
```

**Server → Client (Error - lobby not full)**
```json
{
  "type": "error",
  "message": "Lobby must have 2 players to start"
}
```

---

## 6. Player State (Position + Facing)

Players send their current position and facing direction. The server forwards this to the opponent as `enemyInput`.

**Client → Server**
```json
{
  "type": "interaction",
  "payload": {
    "x": 120.5,
    "y": 88.25,
    "facing": 90
  }
}
```

**Facing** can be either a numeric angle (degrees) or one of: `"up"`, `"down"`, `"left"`, `"right"`.

**Server → Other Client (Forward)**
```json
{
  "type": "enemyInput",
  "payload": {
    "x": 120.5,
    "y": 88.25,
    "facing": 90
  }
}
```

---

## 7. Shooting Input (Separate Message)

Players send shooting state separately. The server forwards it to the opponent as `enemyShooting`.

**Client → Server**
```json
{
  "type": "shooting",
  "payload": {
    "shooting": true
  }
}
```

**Server → Other Client (Forward)**
```json
{
  "type": "enemyShooting",
  "payload": {
    "shooting": true
  }
}
```

---

## 7. Error Responses

**x and y required**
```json
{
  "type": "error",
  "message": "x and y required"
}
```

**Invalid facing**
```json
{
  "type": "error",
  "message": "facing must be angle or direction"
}
```

**Invalid shooting**
```json
{
  "type": "error",
  "message": "shooting must be boolean"
}
```

**Game not running**
```json
{
  "type": "error",
  "message": "Game not running"
}
```

**Invalid JSON**
```json
{
  "type": "error",
  "message": "Invalid JSON"
}
```

---

## Example Flow

```
1. Player A: { "type": "createLobby" }
2. Server → A: { "type": "lobbyCreated", "payload": { "id": "0421", ... } }

3. Player B: { "type": "listLobbies" }
4. Server → B: { "type": "lobbies", "payload": [{ "id": "0421", ... }] }

5. Player B: { "type": "joinLobby", "payload": { "id": "0421" } }
6. Server → B: { "type": "lobbyJoined", "payload": { "id": "0421", "size": 2, ... } }
7. Server → A,B: { "type": "ready" }

8. Player A: { "type": "startLobby", "payload": { "id": "0421" } }
9. Server → A,B: { "type": "gameStarted", "payload": { "state": "running" } }

10. Player A presses Up: { "type": "interaction", "payload": { "button": "up", "state": "pressed" } }
11. Server → B: { "type": "enemyInput", "payload": { "button": "up", "state": "pressed" } }

12. Player A releases Up: { "type": "interaction", "payload": { "button": "up", "state": "released" } }
13. Server → B: { "type": "enemyInput", "payload": { "button": "up", "state": "released" } }

14. Player B presses Shoot: { "type": "interaction", "payload": { "button": "shoot", "state": "pressed" } }
15. Server → A: { "type": "enemyInput", "payload": { "button": "shoot", "state": "pressed" } }
```
