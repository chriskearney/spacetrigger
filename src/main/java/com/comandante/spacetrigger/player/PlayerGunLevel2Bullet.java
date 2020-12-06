package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.Point;
import java.util.Optional;

public class PlayerGunLevel2Bullet extends Projectile {

    public PlayerGunLevel2Bullet(int x, int y, Direction direction) {
        super(x, y, direction, 12, 55);
    }

    @Override
    public void initMissile() {
        loadImage(Assets.PLAYER_GUN_LEVEL_2_BULLET);
    }

    public SpriteSheetAnimation getDamageAnimation(Point point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(32, 32, 8, 8, Assets.PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }
}
