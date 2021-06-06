package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.*;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Optional;

public class PlayerGunLevel2Bullet extends Projectile {

    public PlayerGunLevel2Bullet(double x, double y, Direction direction) {
        super(new PVector(x, y), direction, new PVector(0, 0), 55, Assets.PLAYER_GUN_LEVEL_2_BULLET);
        setVelocity(new PVector(0, -9));
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(32, 32, 8, 8, Assets.PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }

    @Override
    public void move() {
        location.add(velocity);
    }
}
