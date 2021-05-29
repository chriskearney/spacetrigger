package com.comandante.spacetrigger;

import java.awt.Point;
import java.awt.geom.Point2D;

public abstract class Projectile extends Sprite {

    private final Direction direction;
    private final int damage;

    public Projectile(double x, double y, Direction direction, int speed, int damage) {
        super(x, y, speed);
        this.direction = direction;
        this.damage = damage;
        init();
        visible = true;
    }

    public abstract void init();

    public void move() {
        if (direction.equals(Direction.UP)) {
            y -= speed;
        } else {
            y += speed;
        }

        if (y > Main.BOARD_Y) {
            visible = false;
        }

        if (y < 0) {
            visible = false;
        }
    }

    public int getDamage() {
        return damage;
    }

    public abstract SpriteSheetAnimation getDamageAnimation(Point2D point);
}
