package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class AlienScoutMissle extends Projectile {

    public AlienScoutMissle(double x, double y) {
        super(x, y, Direction.DOWN, 4, 250);
    }

    @Override
    public void init() {
        loadImage(Assets.ALIEN_SCOUT_MISSLE);
    }

    @Override
    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienScoutMissleImpactExplosion(point);
    }

}
