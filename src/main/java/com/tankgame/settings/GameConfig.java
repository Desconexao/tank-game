package com.tankgame.settings;

public final class GameConfig {
    // Performance
    public static final int TPS = 60;

    // Grid
    public static final int TILE_SIZE = 75;
    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 13;
    public static final int TANK_SIZE = 60;

    // Bullets
    public static final int BULLET_SIZE = 20;
    public static final double BULLET_SPEED = 8.0;
    // public static final long BULLET_COOL_DOWN = 1_000_000_000L;
    // public static final long BULLET_COOL_DOWN_MS = 1000;
    public static final int DEFAULT_BULLET_DAMAGE = 1;

    // Player
    public static final int PLAYER_START_HEALTH = 5;
    public static final double PLAYER_SPEED = 4.0;
    public static final int PLAYER_START_X = TILE_SIZE;
    public static final int PLAYER_START_Y = TILE_SIZE;
    public static final long PLAYER_BULLET_COOL_DOWN_MS = 500; // 0.5

    // Enemy
    public static final double ENEMY_SPEED = 4.0;
    public static final int ENEMY_START_HEALTH = 2;
    public static final long ENEMY_BULLET_COOL_DOWN_MS = 1000; // 1

    // Graphics opcional -
    public static final int PLAYER_ORIGINAL_SPRITE_SIZE = 13;
    public static final int BULLET_ORIGINAL_SPRITE_SIZE = 3;

    // Tiles
    public static final int BRICK_HP = 4;
    public static final int STEEL_HP = 999;
    public static final int EAGLE_HP = 1;

    //Power up
    public static final long POWERUP_SPAWN_COOLDOWN_MS = 5000;
    public static final int POWERUP_ENTITY_SIZE = 45;
    public static final int POWERUP_SPAWN_BASE_CHANCE = 10000;
    public static final int POWERUP_SPAWN_PROBABILITY = 9990;

    private GameConfig() {}
}
