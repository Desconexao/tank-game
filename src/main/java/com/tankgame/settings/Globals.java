package com.tankgame.settings;

import com.tankgame.graphics.SpriteImport;

public final class Globals {
    public static final int TPS = 60;
    public static final int TILE_SIZE = 75;
    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 13;
    public static final SpriteImport SpriteImporter = new SpriteImport();

    private Globals() {} // prevent instantiation
}
