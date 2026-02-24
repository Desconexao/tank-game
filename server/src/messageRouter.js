const { safeParse } = require("./utils");

class MessageRouter {
  constructor(lobbyManager) {
    this.lobbyManager = lobbyManager;
  }

  handleMessage(socket, raw) {
    const parsed = safeParse(raw);
    if (!parsed) {
      socket.send(JSON.stringify({ type: "error", message: "Invalid JSON" }));
      return;
    }

    const { type, payload } = parsed;

    switch (type) {
      case "createLobby":
        this.handleCreateLobby(socket);
        break;
      case "listLobbies":
        this.handleListLobbies(socket);
        break;
      case "joinLobby":
        this.handleJoinLobby(socket, payload);
        break;
      case "startLobby":
        this.handleStartLobby(socket, payload);
        break;
      case "interaction":
        this.handleInteraction(socket, payload);
        break;
      case "shooting":
        this.handleShooting(socket, payload);
        break;
      default:
        socket.send(JSON.stringify({ type: "error", message: "Unknown message type" }));
        break;
    }
  }

  handleCreateLobby(socket) {
    const lobby = this.lobbyManager.createLobby(socket);
    socket.send(JSON.stringify({ type: "lobbyCreated", payload: lobby.toSummary() }));
  }

  handleListLobbies(socket) {
    const lobbies = this.lobbyManager.listLobbies();
    socket.send(JSON.stringify({ type: "lobbies", payload: lobbies }));
  }

  handleJoinLobby(socket, payload) {
    const lobbyId = payload && payload.id;
    if (!lobbyId) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby id required" }));
      return;
    }

    const lobby = this.lobbyManager.getLobby(lobbyId);
    if (!lobby) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby not found" }));
      return;
    }

    if (lobby.isFull()) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby is full" }));
      return;
    }

    lobby.addClient(socket);
    socket.send(JSON.stringify({ type: "lobbyJoined", payload: lobby.toSummary() }));

    if (lobby.isFull()) {
      lobby.broadcast({ type: "ready" });
    }
  }

  handleStartLobby(socket, payload) {
    const lobbyId = payload && payload.id;
    if (!lobbyId) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby id required" }));
      return;
    }

    const lobby = this.lobbyManager.getLobby(lobbyId);
    if (!lobby) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby not found" }));
      return;
    }

    if (!lobby.isCreator(socket)) {
      socket.send(JSON.stringify({ type: "error", message: "Only creator can start lobby" }));
      return;
    }

    if (!lobby.isFull()) {
      socket.send(JSON.stringify({ type: "error", message: "Lobby must have 2 players to start" }));
      return;
    }

    lobby.setState("running");
    lobby.broadcast({ type: "gameStarted", payload: { state: "running" } });
  }

  handleInteraction(socket, payload) {
    const x = payload && payload.x;
    const y = payload && payload.y;
    const facing = payload && payload.facing;

    if (!Number.isFinite(x) || !Number.isFinite(y)) {
      socket.send(JSON.stringify({ type: "error", message: "x and y required" }));
      return;
    }

    const validFacing = ["up", "down", "left", "right"];
    const facingIsValid = Number.isFinite(facing) || validFacing.includes(facing);
    if (!facingIsValid) {
      socket.send(JSON.stringify({ type: "error", message: "facing must be angle or direction" }));
      return;
    }

    for (const lobby of this.lobbyManager.listLobbies()) {
      const lobbyObj = this.lobbyManager.getLobby(lobby.id);
      if (lobbyObj && lobbyObj.clients.has(socket)) {
        if (lobbyObj.getState() !== "running") {
          socket.send(JSON.stringify({ type: "error", message: "Game not running" }));
          return;
        }

        const enemy = lobbyObj.getEnemy(socket);
        if (enemy && enemy.readyState === 1) {
          enemy.send(JSON.stringify({
            type: "enemyInput",
            payload: {
              x,
              y,
              facing
            }
          }));
        }
        return;
      }
    }
  }

  handleShooting(socket, payload) {
    const shooting = payload && payload.shooting;

    if (typeof shooting !== "boolean") {
      socket.send(JSON.stringify({ type: "error", message: "shooting must be boolean" }));
      return;
    }

    for (const lobby of this.lobbyManager.listLobbies()) {
      const lobbyObj = this.lobbyManager.getLobby(lobby.id);
      if (lobbyObj && lobbyObj.clients.has(socket)) {
        if (lobbyObj.getState() !== "running") {
          socket.send(JSON.stringify({ type: "error", message: "Game not running" }));
          return;
        }

        const enemy = lobbyObj.getEnemy(socket);
        if (enemy && enemy.readyState === 1) {
          enemy.send(JSON.stringify({
            type: "enemyShooting",
            payload: { shooting }
          }));
        }
        return;
      }
    }
  }
}

module.exports = MessageRouter;