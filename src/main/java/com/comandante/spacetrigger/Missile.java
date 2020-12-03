package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.Optional;

public class Missile extends Sprite {

    private final int speed;
    private final Direction direction;
    private final int damage;

    public Missile(int x, int y, Direction direction, int speed, int damage) {
        super(x, y);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        initMissile();
        visible = true;
    }

    public void initMissile() {
        loadImage(Assets.SPACESHIP_MISSLE);
    }

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

    public SpriteSheetAnimation getDamageAnimation(Point point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.SMALL_MISSLE_ANIMATION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }
}
