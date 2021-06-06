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
        super(location, Direction.DOWN, 12, 55);
        rotatedImage = Optional.of(rotateImageByDegrees(image, heading.heading()));
        setVelocity(heading);
        setVisible(true);
        velocity.mult(40);
    }

    @Override
    public void init() {
        loadImage(Assets.ALIEN_BUZZ_BULLET);
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
