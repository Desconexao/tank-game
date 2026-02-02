package com.tankgame.settings;
import static com.tankgame.settings.GameConfig.BULLET_SIZE;
import static com.tankgame.settings.GameConfig.TANK_SIZE;
import static com.tankgame.settings.GameConfig.TILE_SIZE;

// It should be \/ but Java extension is trolling
//import static com.tankgame.settings.GameConfig.*;

public final class SpriteList {
    public static final Object[][] SPRITES_TO_LOAD = {
                { "tank_up_gray",       TANK_SIZE,   TANK_SIZE },
                { "tank_down_gray",     TANK_SIZE,   TANK_SIZE },
                { "tank_left_gray",     TANK_SIZE,   TANK_SIZE },
                { "tank_right_gray",    TANK_SIZE,   TANK_SIZE },
                { "brick",              TILE_SIZE,   TILE_SIZE },
                { "black",              TILE_SIZE,   TILE_SIZE },
                { "steel",              TILE_SIZE,   TILE_SIZE },
                { "bullet_vertical",    BULLET_SIZE, BULLET_SIZE },
                { "tank_up_yellow",     TANK_SIZE,   TANK_SIZE},
                { "tank_down_yellow",   TANK_SIZE,   TANK_SIZE},
                { "tank_left_yellow",   TANK_SIZE,   TANK_SIZE},
                { "tank_right_yellow",  TANK_SIZE,   TANK_SIZE},
                { "tank_up_green",      TANK_SIZE,   TANK_SIZE},
                { "tank_down_green",    TANK_SIZE,   TANK_SIZE},
                { "tank_left_green",    TANK_SIZE,   TANK_SIZE},
                { "tank_right_green",   TANK_SIZE,   TANK_SIZE},
                { "tank_up_red",        TANK_SIZE,   TANK_SIZE},
                { "tank_down_red",      TANK_SIZE,   TANK_SIZE},
                { "tank_left_red",      TANK_SIZE,   TANK_SIZE},
                { "tank_right_red",     TANK_SIZE,   TANK_SIZE},
                { "eagle",              TILE_SIZE,   TILE_SIZE},
                {"tree",                TILE_SIZE,   TILE_SIZE},
                {"water",               TILE_SIZE,   TILE_SIZE},
                {"flag",                TILE_SIZE,   TILE_SIZE}

        };

        private SpriteList(){}
}
