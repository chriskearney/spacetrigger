package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.*;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;
import static com.comandante.spacetrigger.PConstants.TWO_PI;

public class AlienBuzzBullet extends Projectile {

    public AlienBuzzBullet(PVector location, PVector heading) {
        super(location, heading, 55, Assets.ALIEN_BUZZ_BULLET);
        velocity.add(heading);
        velocity.mult(40);
        setVisible(true);
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienNymphBulletImpactExplosion(point);
    }

    @Override
    public void move() {
        location.add(velocity);
        if ((location.x > BOARD_X) || (location.x < 0)) {
            setVisible(false);
        }
        if ((location.y > BOARD_Y) || (location.y < 0)) {
            setVisible(false);
        }
    }
}
