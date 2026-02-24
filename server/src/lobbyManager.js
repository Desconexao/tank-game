const Lobby = require("./lobby");

class LobbyManager {
  constructor() {
    this.lobbies = new Map();
    this.nextId = 0;
  }

  createLobby(creatorSocket) {
    const id = this.generateId();
    const lobby = new Lobby(id, creatorSocket);
    this.lobbies.set(id, lobby);
    return lobby;
  }

  getLobby(id) {
    return this.lobbies.get(id);
  }

  listLobbies() {
    return Array.from(this.lobbies.values()).map((lobby) => lobby.toSummary());
  }

  removeLobby(id) {
    this.lobbies.delete(id);
  }

  removeClientFromLobby(socket) {
    for (const lobby of this.lobbies.values()) {
      if (lobby.clients.has(socket)) {
        if (lobby.isCreator(socket)) {
          this.lobbies.delete(lobby.id);
          return;
        }

        lobby.removeClient(socket);
        if (lobby.isEmpty()) {
          this.lobbies.delete(lobby.id);
        }
        return;
      }
    }
  }

  generateId() {
    const maxAttempts = 10000;
    for (let i = 0; i < maxAttempts; i += 1) {
      this.nextId = (this.nextId + 1) % 10000;
      const id = String(this.nextId).padStart(4, "0");
      if (!this.lobbies.has(id)) {
        return id;
      }
    }

    throw new Error("No available lobby IDs");
  }
}

module.exports = LobbyManager;
