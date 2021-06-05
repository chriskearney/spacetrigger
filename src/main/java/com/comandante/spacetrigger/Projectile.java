package com.comandante.spacetrigger;

import java.awt.Point;
import java.awt.geom.Point2D;

public abstract class Projectile extends Sprite {

    private final Direction direction;
    private final int damage;

    public Projectile(PVector location, Direction direction, double speed, int damage) {
        super(location, speed);
        this.direction = direction;
        this.damage = damage;
        init();
        visible = true;
    }

    public abstract void init();

    public void move() {
        if (direction.equals(Direction.UP)) {
            location.y -= speed;
        } else {
            location.y += speed;
        }

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
