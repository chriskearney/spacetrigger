package com.comandante.spacetrigger;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public abstract class Projectile extends Sprite {

    private final int damage;

    public Projectile(PVector location, PVector heading, int damage, BufferedImage spriteImage) {
        super(location, 0, Optional.ofNullable(spriteImage), Optional.empty(), Optional.empty(), Optional.empty());
        this.damage = damage;
        if (heading != null && heading.heading() != 0) {
            image = cachedRotate(spriteImage, heading.heading());
            calculateSpriteRender();
        }
        visible = true;
    }

    public void update() {
        if (location.y > Main.BOARD_Y) {
            visible = false;
        }

        if (location.y < 0) {
            visible = false;
        }
        super.update();
    }

    public int getDamage() {
        return damage;
    }

    public abstract SpriteSheetAnimation getDamageAnimation(Point2D point);
}
