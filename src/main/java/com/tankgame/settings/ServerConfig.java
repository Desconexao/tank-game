package com.tankgame.settings;

/**
 * Configuration for online server connection
 */
public class ServerConfig {
    // Default server address
    public static final String DEFAULT_SERVER_HOST = "localhost";
    public static final int DEFAULT_SERVER_PORT = 3000;
    public static final String DEFAULT_SERVER_URL = "ws://" + DEFAULT_SERVER_HOST + ":" + DEFAULT_SERVER_PORT;

    // Current server address (can be changed)
    private static String currentServerHost = DEFAULT_SERVER_HOST;
    private static int currentServerPort = DEFAULT_SERVER_PORT;

    public static String getCurrentServerUrl() {
        return "ws://" + currentServerHost + ":" + currentServerPort;
    }

    public static void setServerAddress(String host, int port) {
        currentServerHost = host;
        currentServerPort = port;
    }

    public static void setServerAddress(String hostAndPort) {
        try {
            String[] parts = hostAndPort.split(":");
            if (parts.length == 2) {
                currentServerHost = parts[0];
                currentServerPort = Integer.parseInt(parts[1]);
            }
        } catch (Exception e) {
            System.err.println("Invalid server address format: " + hostAndPort);
        }
    }

    public static void resetToDefault() {
        currentServerHost = DEFAULT_SERVER_HOST;
        currentServerPort = DEFAULT_SERVER_PORT;
    }

    public static String getServerAddress() {
        return currentServerHost + ":" + currentServerPort;
    }
}
