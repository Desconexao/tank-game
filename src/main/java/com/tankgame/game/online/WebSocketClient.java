package com.tankgame.game.online;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

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
        void onEnemyInput(String button, String state);
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
                String button = extractJsonValue(message, "button");
                String state = extractJsonValue(message, "state");
                System.out.println("[SERVER] Enemy input: button=" + button + ", state=" + state);
                messageHandler.onEnemyInput(button, state);
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
        String json = String.format("{\"type\":\"interaction\",\"payload\":{\"button\":\"%s\",\"state\":\"%s\"}}", button, state);
        System.out.println("[CLIENT] Sending input: button=" + button + ", state=" + state);
        sendMessage(json);
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
