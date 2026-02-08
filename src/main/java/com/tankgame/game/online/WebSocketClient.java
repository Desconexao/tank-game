package com.tankgame.game.online;

import java.net.URI;
import java.util.Locale;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.tankgame.utils.Direction;

/**
 * WebSocket client for online gameplay.
 * Handles all network communication with the server.
 */
@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private MessageHandler messageHandler;
    private String serverUrl;
    private boolean connected = false;

    public interface MessageHandler {
        void onLobbyCreated(String id, int size, int capacity, int playerNumber);
        void onLobbyJoined(String id, int size, int capacity, int playerNumber);
        void onReady();
        void onGameStarted();
        void onEnemyState(double x, double y, Direction facing, boolean shooting);
        void onError(String message);
    }

    public WebSocketClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(serverUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to WebSocket server");
        this.connected = true;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);

        if (messageHandler == null)
            return;

        // Parse JSON message based on type
        if (message.contains("\"type\":\"lobbyCreated\"")) {
            try {
                String id = extractJsonValue(message, "id");
                int size = Integer.parseInt(extractJsonValue(message, "size"));
                int capacity = 2; // Default capacity
                String playerNumStr = extractJsonValue(message, "playerNumber");
                int playerNumber = playerNumStr.isEmpty() ? 1 : Integer.parseInt(playerNumStr);
                messageHandler.onLobbyCreated(id, size, capacity, playerNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("\"type\":\"lobbyJoined\"")) {
            try {
                String id = extractJsonValue(message, "id");
                int size = Integer.parseInt(extractJsonValue(message, "size"));
                int capacity = 2; // Default capacity
                String playerNumStr = extractJsonValue(message, "playerNumber");
                int playerNumber = playerNumStr.isEmpty() ? 2 : Integer.parseInt(playerNumStr);
                messageHandler.onLobbyJoined(id, size, capacity, playerNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("\"type\":\"ready\"")) {
            messageHandler.onReady();
        } else if (message.contains("\"type\":\"gameStarted\"")) {
            messageHandler.onGameStarted();
        } else if (message.contains("\"type\":\"enemyInput\"")) {
            try {
                String xValue = extractJsonValue(message, "x");
                String yValue = extractJsonValue(message, "y");
                String facingValue = extractJsonValue(message, "facing");
                String shootingValue = extractJsonValue(message, "shooting");

                double x = xValue.isEmpty() ? 0.0 : Double.parseDouble(xValue);
                double y = yValue.isEmpty() ? 0.0 : Double.parseDouble(yValue);
                Direction facing = parseFacing(facingValue);
                boolean shooting = "true".equalsIgnoreCase(shootingValue);

                System.out.println("[SERVER] Enemy state: x=" + x + ", y=" + y + ", facing=" + facing + ", shooting=" + shooting);
                messageHandler.onEnemyState(x, y, facing, shooting);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("\"type\":\"error\"")) {
            try {
                String errorMsg = extractJsonValue(message, "message");
                messageHandler.onError(errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1)
            return "";
        start += search.length();

        // Skip whitespace and quotes
        while (start < json.length()
                && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }

        int end = start;
        while (end < json.length() && json.charAt(end) != '\"' && json.charAt(end) != ','
                && json.charAt(end) != '}') {
            end++;
        }

        return json.substring(start, end).trim();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Disconnected: " + closeReason);
        this.connected = false;
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public void createLobby() {
        String json = "{\"type\":\"createLobby\"}";
        sendMessage(json);
    }

    public void joinLobby(String lobbyId) {
        String json = String.format("{\"type\":\"joinLobby\",\"payload\":{\"id\":\"%s\"}}", lobbyId);
        sendMessage(json);
    }

    public void startLobby(String lobbyId) {
        String json = String.format("{\"type\":\"startLobby\",\"payload\":{\"id\":\"%s\"}}", lobbyId);
        sendMessage(json);
    }

    public void sendInteraction(String button, String state) {
        throw new UnsupportedOperationException("sendInteraction(button, state) is deprecated");
    }

    public void sendPlayerState(double x, double y, Direction facing, boolean shooting) {
        String facingValue = facing != null ? facing.name().toLowerCase(Locale.ROOT) : "up";
        String json = String.format(Locale.US,
                "{\"type\":\"interaction\",\"payload\":{\"x\":%.3f,\"y\":%.3f,\"facing\":\"%s\",\"shooting\":%s}}",
                x, y, facingValue, shooting ? "true" : "false");
        System.out.println("[CLIENT] Sending state: x=" + x + ", y=" + y + ", facing=" + facingValue + ", shooting=" + shooting);
        sendMessage(json);
    }

    private Direction parseFacing(String facingValue) {
        if (facingValue == null || facingValue.isEmpty()) {
            return null;
        }

        String value = facingValue.trim().toLowerCase(Locale.ROOT);
        switch (value) {
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            default:
                break;
        }

        try {
            double angle = Double.parseDouble(value) % 360.0;
            if (angle < 0) {
                angle += 360.0;
            }

            if (angle >= 45.0 && angle < 135.0) {
                return Direction.RIGHT;
            }
            if (angle >= 135.0 && angle < 225.0) {
                return Direction.DOWN;
            }
            if (angle >= 225.0 && angle < 315.0) {
                return Direction.LEFT;
            }
            return Direction.UP;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void disconnect() {
        if (session != null) {
            try {
                session.close();
                connected = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connected && session != null && session.isOpen();
    }
}
