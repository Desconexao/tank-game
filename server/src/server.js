const http = require("http");
const { WebSocketServer } = require("ws");
const LobbyManager = require("./lobbyManager");
const MessageRouter = require("./messageRouter");

const PORT = process.env.PORT || 3000;

const server = http.createServer();
const wss = new WebSocketServer({ server });

const lobbyManager = new LobbyManager();
const router = new MessageRouter(lobbyManager);

wss.on("connection", (socket) => {
  socket.on("message", (data) => {
    router.handleMessage(socket, data.toString());
  });

  socket.on("close", () => {
    lobbyManager.removeClientFromLobby(socket);
  });
});

server.listen(PORT, () => {
  console.log(`WebSocket server running on :${PORT}`);
});
