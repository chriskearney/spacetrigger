package com.comandante.spacetrigger.player;


import com.comandante.spacetrigger.*;
import com.comandante.spacetrigger.events.HealthPickUpEvent;
import com.comandante.spacetrigger.events.MisslePickUpEvent;
import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.events.PlayerShipShieldUpdateEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class PlayerShip extends Sprite {

    private double dx;
    private double dy;

    private int missleCapacity = 10;
    private int currentMissles = 3;

    private int maxShield = 100;
    private int currentShield = 100;

    private final List<Projectile> projectiles = Lists.newArrayList();
    private final SpriteSheetAnimation exhaust = Assets.getPlayerShipExhaustAnimation();
    private final Shield shield = new Shield();
    private final EventBus eventBus;

    private int ticks;

    public PlayerShip(EventBus eventBus) {
        super(0, 0, 500, 0);
        initSpaceShip();
        super.x = BOARD_X / 2;
        super.y = BOARD_Y - getHeight() * 4;
        this.eventBus = eventBus;
        this.visible = true;
    }

    private void initSpaceShip() {
        loadImage(Assets.PLAYER_SHIP);
        loadExplosion(Assets.getPlayerShipExplosionAnimation());
    }

    public void move() {
        if (isExploding) {
            return;
        }

        if (currentShield <= 0) {
            shield.setVisible(false);
        }

        if (isShield()) {
            if (ticks % 2 == 0) {
                currentShield--;
                eventBus.post(new PlayerShipShieldUpdateEvent(Math.round(currentShield * 100.f) / maxShield));
            }
        } else {
            if (currentShield < maxShield) {
                if (ticks % 10 == 0) {
                    currentShield++;
                    eventBus.post(new PlayerShipShieldUpdateEvent(Math.round(currentShield * 100.f) / maxShield));
                }
            }
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
        ticks++;
    }

    public void addMissles(int number) {
        currentMissles += number;
    }

    public List<Projectile> getMissiles() {
        return projectiles;
    }

    public void fireMissle() {
        if (currentMissles == 0) {
            return;
        }
        projectiles.add(new PlayerMissleLevel1Bullet((x + width / 2) - 8, (y + height / 2) - 20));
        currentMissles--;
    }

    public void fireGun() {
        projectiles.add(new PlayerGunLevel2Bullet((x + width / 2) - 22, (y + height / 2) - 15, Direction.UP));
        projectiles.add(new PlayerGunLevel2Bullet((x + width / 2) + 3, (y + height / 2) - 15, Direction.UP));
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

        if (key == KeyEvent.VK_M) {
            if (currentShield > 24) {
                shield.setVisible(true);
            }
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

        if (key == KeyEvent.VK_M) {
            shield.setVisible(false);
        }
    }

    public SpriteSheetAnimation getExhaust() {
        return exhaust;
    }

    public boolean isMovement() {
        return dy != 0 || dx != 0;
    }

    public boolean isShield() {
        return shield.isVisible();
    }

    public Sprite getShield() {
        return shield;
    }

    public int getCurrentMissles() {
        return currentMissles;
    }

    @Subscribe
    public void processMisslePickUp(MisslePickUpEvent misslePickUpEvent) {
        this.currentMissles += misslePickUpEvent.getAmount();
    }

    @Subscribe
    public void processHealthPickUp(HealthPickUpEvent healthPickUpEvent) {
        eventBus.post(new PlayerShipHealthUpdateEvent(calculateHitPointsPercentAfterHealthApplied(healthPickUpEvent.getAmt())));
    }
}

