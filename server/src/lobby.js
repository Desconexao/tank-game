class Lobby {
  constructor(id, creatorSocket) {
    this.id = id;
    this.creatorSocket = creatorSocket;
    this.clients = new Set();
    this.state = "waiting";
    this.addClient(creatorSocket);
  }

  addClient(socket) {
    this.clients.add(socket);
  }

  removeClient(socket) {
    this.clients.delete(socket);
  }

  isFull() {
    return this.clients.size >= 2;
  }

  isEmpty() {
    return this.clients.size === 0;
  }

  isCreator(socket) {
    return this.creatorSocket === socket;
  }

  setState(state) {
    this.state = state;
  }

  getState() {
    return this.state;
  }

  getEnemy(socket) {
    for (const client of this.clients) {
      if (client !== socket) {
        return client;
      }
    }
    return null;
  }

  toSummary() {
    return {
      id: this.id,
      size: this.clients.size,
      full: this.isFull(),
      state: this.state
    };
  }

  broadcast(data) {
    const payload = JSON.stringify(data);
    this.clients.forEach((client) => {
      if (client.readyState === 1) {
        client.send(payload);
      }
    });
  }
}

module.exports = Lobby;
