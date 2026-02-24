# Player State Sync System - Implementation Guide

## Overview

The input system now sends **player state** instead of button press/release events. Each update includes:

- `x`, `y` position coordinates
- `facing` direction (angle or cardinal direction)

Shooting is sent as a **separate message**. The server forwards position/facing as `enemyInput` and shooting as `enemyShooting`.

---

## Protocol Details

### Client → Server (Position Update)

**Message Format:**
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

---

### Server → Client (Enemy Position Update)

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

### Client → Server (Shooting Update)

```json
{
  "type": "shooting",
  "payload": {
    "shooting": true
  }
}
```

---

### Server → Client (Enemy Shooting Update)

```json
{
  "type": "enemyShooting",
  "payload": {
    "shooting": true
  }
}
```

---

## Implementation Guide for Clients

### 1. Track Local State

```javascript
const playerState = {
  x: 0,
  y: 0,
  facing: 0
};
```

### 2. Update Shooting Input

```javascript
document.addEventListener("keydown", (e) => {
  if (e.key === " " || e.key === "Space") {
    ws.send(JSON.stringify({
      type: "shooting",
      payload: { shooting: true }
    }));
  }
});

document.addEventListener("keyup", (e) => {
  if (e.key === " " || e.key === "Space") {
    ws.send(JSON.stringify({
      type: "shooting",
      payload: { shooting: false }
    }));
  }
});
```

### 3. Send State at a Fixed Tick

```javascript
function sendState(ws) {
  ws.send(JSON.stringify({
    type: "interaction",
    payload: {
      x: playerState.x,
      y: playerState.y,
      facing: playerState.facing,
    }
  }));
}

setInterval(() => sendState(ws), 50);
```

### 4. Handle Enemy State

```javascript
const enemyState = {
  x: 0,
  y: 0,
  facing: 0,
  shooting: false
};

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  if (data.type === "enemyInput") {
    Object.assign(enemyState, data.payload);
  }
  if (data.type === "enemyShooting") {
    enemyState.shooting = data.payload.shooting;
  }
};
```