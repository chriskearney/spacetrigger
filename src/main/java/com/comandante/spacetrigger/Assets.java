package com.comandante.spacetrigger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Assets {

    public static final BufferedImage ALIEN_SCOUT;
    public static final BufferedImage ALIEN_SCOUT_EXPLOSION;
    public static final BufferedImage ALIEN_SCOUT_MISSLE;

    public static final BufferedImage ALIEN_NYMPH;
    public static final BufferedImage ALIEN_NYMPH_EXPLOSION;

    public static final BufferedImage SPACESHIP;
    public static final BufferedImage SPACESHIP_EXPLOSION;
    public static final BufferedImage SPACESHIP_MISSLE;

    public static final BufferedImage MACHINEGUN_MISSLE;


    public static final BufferedImage BOARD_BACKGROUND_1;

    public static final BufferedImage SMALL_MACHINEGUN_ANIMATION;
    public static final BufferedImage SMALL_MISSLE_ANIMATION;

    public static final BufferedImage RED_WARP;

    static {

        ALIEN_SCOUT = loadImage("alien-scout.png");
        ALIEN_SCOUT_EXPLOSION = loadImage("alien-scout-explosion.png");
        ALIEN_SCOUT_MISSLE = loadImage("alien-scout-missle.png");

        ALIEN_NYMPH = loadImage("alien-nymph.png");
        ALIEN_NYMPH_EXPLOSION = loadImage("alien-nymph-explosion.png");

        SPACESHIP = loadImage("spaceship.png");
        SPACESHIP_EXPLOSION = loadImage("spaceship-explosion.png");
        SPACESHIP_MISSLE = loadImage("spaceship-missle.png");

        MACHINEGUN_MISSLE = loadImage("small-machinegun-bullet.png");

        SMALL_MACHINEGUN_ANIMATION = loadImage("small-machinegun-explosion-new.png");

        SMALL_MISSLE_ANIMATION = loadImage("small-missle-explosion.png");

        BOARD_BACKGROUND_1 = loadImage("spacebackground.png");

        RED_WARP = loadImage("redwarp-small.png");
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
