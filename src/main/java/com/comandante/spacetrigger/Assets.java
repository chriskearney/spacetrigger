package com.comandante.spacetrigger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Assets {

    public static final BufferedImage ALIEN_SCOUT;
    public static final BufferedImage ALIEN_SCOUT_EXPLOSION;
    public static final BufferedImage ALIEN_SCOUT_MISSLE;
    public static final BufferedImage ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION;

    public static final BufferedImage ALIEN_NYMPH;
    public static final BufferedImage ALIEN_NYMPH_EXPLOSION;
    public static final BufferedImage ALIEN_NYMPH_BULLET;
    public static final BufferedImage ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION;

    public static final BufferedImage PLAYER_SHIP;
    public static final BufferedImage PLAYER_SHIP_EXPLOSION;
    public static final BufferedImage PLAYER_MISSLE_LEVEL_1_BULLET;

    public static final BufferedImage PLAYER_GUN_LEVEL_2_BULLET;

    public static final BufferedImage BOARD_BACKGROUND_1;

    public static final BufferedImage PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION;
    public static final BufferedImage PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION;

    public static final BufferedImage ALIEN_NYMPH_WARP;

    public static final BufferedImage ALIEN_SCOUT_WARP;

    static {

        ALIEN_SCOUT = loadImage("alien-scout.png");
        ALIEN_SCOUT_EXPLOSION = loadImage("alien-scout-explosion.png");
        ALIEN_SCOUT_MISSLE = loadImage("alien-scout-missle.png");
        ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION = loadImage("alien-scout-missle-impact-explosion.png");
        ALIEN_SCOUT_WARP = loadImage("alien-scout-warp.png");

        ALIEN_NYMPH = loadImage("alien-nymph.png");
        ALIEN_NYMPH_EXPLOSION = loadImage("alien-nymph-explosion.png");
        ALIEN_NYMPH_BULLET = loadImage("alien-nymph-bullet.png");
        ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION = loadImage("alien-nymph-bullet-impact-explosion.png");
        ALIEN_NYMPH_WARP = loadImage("alien-nymph-warp.png");

        PLAYER_SHIP = loadImage("player-ship.png");
        PLAYER_SHIP_EXPLOSION = loadImage("player-ship-explosion.png");
        PLAYER_GUN_LEVEL_2_BULLET = loadImage("player-gun-level-2-bullet.png");
        PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION = loadImage("player-gun-level-2-bullet-impact-explosion.png");
        PLAYER_MISSLE_LEVEL_1_BULLET = loadImage("player-missle-level-1-bullet.png");
        PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION = loadImage("player-missle-level-1-impact-explosion.png");

        BOARD_BACKGROUND_1 = loadImage("board-background-1.png");
    }

    public static BufferedImage loadImage(String imageFilename) {
        try {
            InputStream imageStream = Assets.class.getClassLoader().getResourceAsStream(imageFilename);
            return ImageIO.read(imageStream);
        } catch (Exception e) {
            throw new RuntimeException(imageFilename + e);
        }
    }

}
