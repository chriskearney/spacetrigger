package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class PlayerMissleLevel1Bullet extends Projectile {

    public PlayerMissleLevel1Bullet(double x, double y) {
        super(x, y, Direction.UP, 4, 1000);
    }

    public void init() {
        loadImage(Assets.PLAYER_MISSLE_LEVEL_1_BULLET);
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }
}
