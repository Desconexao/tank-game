package com.tankgame.managers;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<String, Font> fontCache = new HashMap<>();
    private static final String FONT_PATH = "assets/ttf/";

    public static Font getFont(String fontName, float size) {
        // Load font if not cached
        if (!fontCache.containsKey(fontName)) {
            loadFont(fontName);
        }

        Font baseFont = fontCache.get(fontName);
        return baseFont != null ? baseFont.deriveFont(size) : new Font("Arial", Font.BOLD, (int) size);
    }

    private static void loadFont(String fontName) {
        try {
            String filePath = FONT_PATH + fontName + ".ttf";
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filePath));
            fontCache.put(fontName, font);
        } catch (Exception e) {
            System.err.println("Failed to load font '" + fontName + "': " + e.getMessage());
            fontCache.put(fontName, null);
        }
    }
}