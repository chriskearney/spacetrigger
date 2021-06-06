package com.comandante.spacetrigger;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Projectile extends Sprite {

    private final int damage;

    public Projectile(PVector location, PVector heading, int damage, BufferedImage spriteImage) {
        super(location);
        this.damage = damage;
        init(spriteImage, heading.heading());
        visible = true;
    }

    public void init(BufferedImage spriteImage, double heading) {
        if (heading != 0) {
            loadImage(GfxUtil.rotateImageByDegrees(spriteImage, heading));
        } else {
            loadImage(spriteImage);
        }
    }

    public void move() {
        if (location.y > Main.BOARD_Y) {
            visible = false;
        }

        if (location.y < 0) {
            visible = false;
        }
    }

    public int getDamage() {
        return damage;
    }

    public abstract SpriteSheetAnimation getDamageAnimation(Point2D point);
}
