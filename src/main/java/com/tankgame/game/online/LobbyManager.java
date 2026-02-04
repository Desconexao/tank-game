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

@ClientEndpoint
public class LobbyManager {
    private Session session;
    private MessageHandler messageHandler;

    public interface MessageHandler {
        void onLobbyCreated(String id, int count, int capacity);
        void onLobbyJoined(String id, int count, int capacity);
        void onReady();
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void connect(String serverUrl) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(serverUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        
        if (messageHandler == null) return;
        
        // Parse JSON message
        if (message.contains("\"type\":\"lobbyCreated\"")) {
            try {
                String id = extractJsonValue(message, "id");
                int size = Integer.parseInt(extractJsonValue(message, "size"));
                int capacity = 2; // Default capacity
                messageHandler.onLobbyCreated(id, size, capacity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("\"type\":\"lobbyJoined\"")) {
            try {
                String id = extractJsonValue(message, "id");
                int size = Integer.parseInt(extractJsonValue(message, "size"));
                int capacity = 2; // Default capacity
                messageHandler.onLobbyJoined(id, size, capacity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("\"type\":\"ready\"")) {
            messageHandler.onReady();
        }
    }
    
    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        
        // Skip whitespace and quotes
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }
        
        int end = start;
        while (end < json.length() && json.charAt(end) != '\"' && json.charAt(end) != ',' && json.charAt(end) != '}') {
            end++;
        }
        
        return json.substring(start, end).trim();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Disconnected: " + closeReason);
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
        String json = String.format(
            "{\"type\":\"joinLobby\",\"payload\":{\"id\":\"%s\"}}",
            lobbyId
        );
        sendMessage(json);
    }

    public void disconnect() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
