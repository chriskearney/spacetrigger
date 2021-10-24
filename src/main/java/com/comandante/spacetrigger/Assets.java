package com.comandante.spacetrigger;

import com.comandante.spacetrigger.sound.SoundEffectService;
import com.google.common.io.ByteStreams;
import tinysound.TinySound;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

public class Assets {

    public static final BufferedImage ALIEN_SCOUT;
    public static final BufferedImage ALIEN_SCOUT_EXPLOSION;
    public static final SoundEffectService.PlaySound ALIEN_SCOUT_EXPLOSION_SOUND;
    public static final BufferedImage ALIEN_SCOUT_MISSLE;
    public static final BufferedImage ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION;
    public static final SoundEffectService.PlaySound ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION_SOUND;
    public static final BufferedImage ALIEN_SCOUT_WARP;

    public static final BufferedImage ALIEN_NYMPH;
    public static final BufferedImage ALIEN_NYMPH_EXPLOSION;
    public static final BufferedImage ALIEN_NYMPH_BULLET;
    public static final BufferedImage ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION;
    public static final BufferedImage ALIEN_NYMPH_WARP;

    public static final BufferedImage PLAYER_SHIP;
    public static final BufferedImage PLAYER_SHIP_EXPLOSION;
    public static final BufferedImage PLAYER_SHIP_SHIELD;
    public static final BufferedImage PLAYER_SHIP_ANIMATED_SHIELD;
    public static final BufferedImage PLAYER_SHIP_EXHAUST;
    public static final BufferedImage PLAYER_MISSLE_LEVEL_1_BULLET;

    public static final BufferedImage PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION;
    public static final BufferedImage PLAYER_GUN_LEVEL_2_BULLET;
    public static final SoundEffectService.PlaySound PLAYER_GUN_LEVEL_2_BULLET_SOUND;

    public static final BufferedImage PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION;
    public static final SoundEffectService.PlaySound PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION_SOUND;

    public static final BufferedImage BOARD_BACKGROUND_3;
    public static final BufferedImage BOARD_BACKGROUND_2;
    public static final BufferedImage BOARD_BACKGROUND_1;

    public static final BufferedImage MISSLE_DROP;
    public static final BufferedImage HEALTH_DROP;


    public static final BufferedImage ALIEN_BUZZ;
    public static final BufferedImage ALIEN_BUZZ_BULLET;

    public static final BufferedImage PLAYER_HEALTH_BAR_FRAME;
    public static final BufferedImage PLAYER_HEALTH_BAR_EMPTY;
    public static final BufferedImage PLAYER_HEALTH_BAR_FULL;
    public static final BufferedImage PLAYER_SHIELD_BAR_FULL;

    public static final SoundEffectService.PlaySound PLAYER_SHIELD_IMPACT_SOUND;

    public static final BufferedImage TRANSPARENT_ONE_PIXEL;

    static {

        TRANSPARENT_ONE_PIXEL = GfxUtil.createTransparentBufferedImage(1, 1);

        ALIEN_SCOUT = loadImage("alien-scout.png");
        ALIEN_SCOUT_EXPLOSION = loadImage("alien-scout-explosion.png");
        ALIEN_SCOUT_EXPLOSION_SOUND = loadSound("alien-scout-explosion.wav");
        ALIEN_SCOUT_MISSLE = loadImage("alien-scout-missle.png");
        ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION = loadImage("alien-scout-missle-impact-explosion.png");
        ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION_SOUND = loadSound("alien-scout-missle-impact-explosion.wav");
        ALIEN_SCOUT_WARP = loadImage("alien-scout-warp.png");

        ALIEN_BUZZ = loadImage("alien-buzz.png");
        ALIEN_BUZZ_BULLET = loadImage("alien-buzz-bullet.png");


        ALIEN_NYMPH = loadImage("alien-nymph.png");
        ALIEN_NYMPH_EXPLOSION = loadImage("alien-nymph-explosion.png");
        ALIEN_NYMPH_BULLET = loadImage("alien-nymph-bullet.png");
        ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION = loadImage("alien-nymph-bullet-impact-explosion.png");
        ALIEN_NYMPH_WARP = loadImage("alien-nymph-warp.png");

        PLAYER_SHIP = loadImage("player-ship.png");
        PLAYER_SHIP_EXPLOSION = loadImage("player-ship-explosion.png");
        PLAYER_SHIP_SHIELD = loadImage("player-ship-shield.png");
        PLAYER_SHIP_ANIMATED_SHIELD = loadImage("player-ship-animated-shield.png");

        PLAYER_SHIP_EXHAUST = loadImage("player-ship-exhaust.png");
        PLAYER_GUN_LEVEL_2_BULLET = loadImage("player-gun-level-2-bullet.png");
        PLAYER_GUN_LEVEL_2_BULLET_SOUND = loadSound("player-missle-level-1-bullet.wav");

        PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION = loadImage("player-gun-level-2-bullet-impact-explosion.png");
        PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION_SOUND = loadSound("player-gun-level-2-bullet-impact-explosion.wav");

        PLAYER_MISSLE_LEVEL_1_BULLET = loadImage("player-missle-level-1-bullet.png");
        PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION = loadImage("player-missle-level-1-impact-explosion.png");

        BOARD_BACKGROUND_1 = loadImage("board-background-1.png");
        BOARD_BACKGROUND_2 = loadImage("board-background-2.png");
        BOARD_BACKGROUND_3 = loadImage("board-background-3.png");

        MISSLE_DROP = loadImage("missle-drop.png");
        HEALTH_DROP = loadImage("health-drop.png");


        PLAYER_HEALTH_BAR_FRAME = Assets.loadImage("player-health-bar/player-health-bar-frame.png");
        PLAYER_HEALTH_BAR_FULL = Assets.loadImage("player-health-bar/player-health-bar-full.png");
        PLAYER_HEALTH_BAR_EMPTY = Assets.loadImage("player-health-bar/player-health-bar-empty.png");
        PLAYER_SHIELD_BAR_FULL = Assets.loadImage("player-health-bar/player-shield-bar-full.png");

        PLAYER_SHIELD_IMPACT_SOUND = loadSound("player-ship-shield-impact.wav");

    }

    public static BufferedImage loadImage(String imageFilename) {
        try {
            InputStream imageStream = Assets.class.getClassLoader().getResourceAsStream(imageFilename);
            return ImageIO.read(imageStream);
        } catch (Exception e) {
            throw new RuntimeException(imageFilename, e);
        }
    }

    public static SoundEffectService.PlaySound loadSound(String soundFileName) {
        return loadSound(soundFileName, 0);
    }

    public static SoundEffectService.PlaySound loadSound(String soundFileName, int numLoops) {
        try {
            InputStream soundStream = Assets.class.getClassLoader().getResourceAsStream(soundFileName);
            return new SoundEffectService.PlaySound(TinySound.loadSound(soundStream), numLoops);
        } catch (Exception e) {
            throw new RuntimeException(soundFileName, e);
        }
    }

    public static SpriteSheetAnimation getPlayerShipExplosionAnimation() {
        return new SpriteSheetAnimation(376, 376, 8, 8, Assets.PLAYER_SHIP_EXPLOSION, 2, 3);
    }

    public static SpriteSheetAnimation getPlayerShipExhaustAnimation() {
        return new SpriteSheetAnimation(8, 15, 8, 1, Assets.PLAYER_SHIP_EXHAUST, 0, 3, true, Optional.empty());
    }

    public static SpriteSheetAnimation getAlienScoutExplosionAnimation() {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(188, 188, 8, 8, Assets.ALIEN_SCOUT_EXPLOSION, 2, 3);
        spriteSheetAnimation.setPlaySound(ALIEN_SCOUT_EXPLOSION_SOUND);
        return spriteSheetAnimation;
    }

    public static SpriteSheetAnimation getAlientScoutWarpAnimation() {
        return new SpriteSheetAnimation(160, 182, 7, 1, Assets.ALIEN_SCOUT_WARP, 0, 4);
    }

    public static SpriteSheetAnimation getAlienScoutMissleImpactExplosion(Point2D point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        spriteSheetAnimation.setPlaySound(Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION_SOUND);
        return spriteSheetAnimation;
    }

    public static SpriteSheetAnimation getAlienScoutMissleExplosion() {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION, 2, 3);
        spriteSheetAnimation.setPlaySound(Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION_SOUND);
        return spriteSheetAnimation;
    }

    public static SpriteSheetAnimation getAlienNymphBulletImpactExplosion(Point2D point) {
        return new SpriteSheetAnimation(32, 32, 8, 8, Assets.ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
    }

    public static SpriteSheetAnimation getAlienNymphWarpAnimation() {
        return new SpriteSheetAnimation(64, 64, 9, 1, Assets.ALIEN_NYMPH_WARP, 0, 5);
    }

    public static SpriteSheetAnimation getAlienNymphExplosion() {
        return new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3);
    }

    public static SpriteSheetAnimation getAlienBuzzWarpAnimation() {
        return new SpriteSheetAnimation(64, 64, 9, 1, Assets.ALIEN_NYMPH_WARP, 0, 5);
    }

    public static SpriteSheetAnimation getAlienBuzzAnimation() {
        return new SpriteSheetAnimation(32, 32, 6, 1, Assets.ALIEN_BUZZ, 0, 7, true, Optional.empty());
    }

    public static SpriteSheetAnimation getAlienBuzzExplosion() {
        return new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3);
    }

    public static SpriteSheetAnimation getShieldAnimation() {
        return new SpriteSheetAnimation(64, 64, 11, 1, Assets.PLAYER_SHIP_ANIMATED_SHIELD, 0, 3, true, Optional.empty());
    }
}
