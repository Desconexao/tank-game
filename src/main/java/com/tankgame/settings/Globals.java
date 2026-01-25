package com.tankgame.settings;

import com.tankgame.graphics.SpriteImport;

public final class Globals {
    public static final int TPS = 60;
    public static final int TILE_SIZE = 75;
    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 13;
    public static final int PLAYER_ORIGINAL_SPRITE_SIZE = 13;
    public static final int BULLET_ORIGINAL_SPRITE_SIZE = 3;
    public static final int BULLET_SIZE = 30;

    public static final long BULLET_COOL_DOWN = 1_000_000_000L; // 1000 ms

    


    public static final SpriteImport SpriteImporter = new SpriteImport();

    private Globals() {} // prevent instantiation
}
