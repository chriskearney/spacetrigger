package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.Optional;

public abstract class Projectile extends Sprite {

    private final Direction direction;
    private final int damage;

    public Projectile(int x, int y, Direction direction, int speed, int damage) {
        super(x, y, speed);
        this.direction = direction;
        this.damage = damage;
        initMissile();
        visible = true;
    }

    public abstract void initMissile();

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

    public abstract SpriteSheetAnimation getDamageAnimation(Point point);
}
