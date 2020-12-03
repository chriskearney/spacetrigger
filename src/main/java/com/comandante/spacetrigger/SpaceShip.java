package com.comandante.spacetrigger;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class SpaceShip extends Sprite {

    private int dx;
    private int dy;

    private int missleCapacity = 10;
    private int currentMissles = 3;

    private final List<Missile> missiles = new ArrayList<>();

    public SpaceShip() {
        super(0, 0, 500);
        initSpaceShip();
        super.x = BOARD_X / 2;
        super.y = BOARD_Y - getHeight() * 4;
        visible = true;
    }

    private void initSpaceShip() {
        loadImage(Assets.SPACESHIP);
        loadExplosion(new SpriteSheetAnimation(376, 376, 8, 8, Assets.SPACESHIP_EXPLOSION, 2, 3));
    }

    public void move() {
        if (isExploding) {
            return;
        }

        if (dx != 0 || dy != 0) {
            if (dx != 0) {
                if ((x + dx) <= 0) {
                    x = 0;
                    dx = 0;
                    return;
                }

                if ((x + dx) >= (BOARD_X - width)) {
                    x = (BOARD_X - width);
                    dx = 0;
                    return;
                }
            }

            if (dy != 0) {
                if ((y + dy) <= 0) {
                    y = 0;
                    dy = 0;
                    return;
                }

                if ((y + dy) >= (BOARD_Y - height * 2)) {
                    y = (BOARD_Y - height * 2);
                    dy = 0;
                    return;
                }
            }

            x += dx;
            y += dy;
        }
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public void fireMissle() {
        if (currentMissles == 0) {
            return;
        }
        missiles.add(new Missile((x + width / 2) - 8, (y + height / 2) - 20, Direction.UP, 4, 1000));
        currentMissles--;
    }

    public void fireGun() {
        missiles.add(new MachineGunMissle((x + width / 2) - 22, (y + height / 2) - 15, Direction.UP));
        missiles.add(new MachineGunMissle((x + width / 2) + 3, (y + height / 2) - 15, Direction.UP));
    }

    public void keyPressed(KeyEvent e) {

        if (isExploding || !isVisible()) {
            return;
        }

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            fireGun();
        }

        if (key == KeyEvent.VK_Z) {
            fireMissle();
        }

        if (key == KeyEvent.VK_A) {
            dx = -4;
        }

        if (key == KeyEvent.VK_D) {
            dx = 4;
        }

        if (key == KeyEvent.VK_W) {
            dy = -4;
        }

        if (key == KeyEvent.VK_S) {
            dy = 4;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            // If the current speed is headed left, clear it.  dont otherwise it interferes with another key press.
            if (dx < 0) {
                dx = 0;
            }
        }

        if (key == KeyEvent.VK_D) {
            // If the current speed is headed right, clear it.  dont otherwise it interferes with another key press.
            if (dx > 0) {
                dx = 0;
            }
        }

        if (key == KeyEvent.VK_W) {
            // If the current speed is headed up, clear it.  dont otherwise it interferes with another key press.
            if (dy < 0) {
                dy = 0;
            }
        }

        if (key == KeyEvent.VK_S) {
            // If the current speed is headed down, clear it.  dont otherwise it interferes with another key press.
            if (dy > 0) {
                dy = 0;
            }
        }
    }

    public int getCurrentMissles() {
        return currentMissles;
    }
}

