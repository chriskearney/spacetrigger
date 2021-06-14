package com.comandante.spacetrigger.player;


import com.comandante.spacetrigger.*;
import com.comandante.spacetrigger.events.HealthPickUpEvent;
import com.comandante.spacetrigger.events.MisslePickUpEvent;
import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
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
        super(new PVector(0, 0),
                500,
                Optional.ofNullable(Assets.PLAYER_SHIP),
                Optional.empty(),
                Optional.of(Assets.getPlayerShipExplosionAnimation()),
                Optional.empty());

        this.location = new PVector(BOARD_X / 2, BOARD_Y - getHeight() * 4);
        this.velocity = new PVector(0, 0);
        this.eventBus = eventBus;
        this.visible = true;
    }

    public void update() {
        try {
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

            if (velocity.x != 0 || velocity.y != 0) {
                if (velocity.x != 0) {
                    if ((location.x + velocity.x) <= 0) {
                        location.x = 0;
                        setVelocity(new PVector(0, velocity.y));
                        return;
                    }

                    if ((location.x + velocity.x) >= (BOARD_X - getWidth())) {
                        location.x = (BOARD_X - getWidth());
                        setVelocity(new PVector(0, velocity.y));
                        return;
                    }
                }

                if (velocity.y != 0) {
                    if ((location.y + velocity.y) <= 0) {
                        location.y = 0;
                        setVelocity(new PVector(velocity.x, 0));
                        return;
                    }

                    if ((location.y + velocity.y) >= (BOARD_Y - getHeight() * 2)) {
                        location.y = (BOARD_Y - getHeight() * 2);
                        setVelocity(new PVector(velocity.x, 0));
                        return;
                    }
                }

                location.add(velocity);
            }
            ticks++;
            eventBus.post(new PlayerShipLocationUpdateEvent(location));
        } finally {
            super.update();
        }
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
        projectiles.add(new PlayerMissleLevel1Bullet((location.x + getWidth() / 2) - 8, (location.y + getHeight() / 2) - 20));
        currentMissles--;
    }

    public void fireGun() {
        projectiles.add(new PlayerGunLevel2Bullet((location.x + getWidth() / 2) - 22, (location.y + getHeight() / 2) - 15));
        projectiles.add(new PlayerGunLevel2Bullet((location.x + getWidth() / 2) + 3, (location.y + getHeight() / 2) - 15));
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
            setVelocity(new PVector(-4, velocity.y));
        }

        if (key == KeyEvent.VK_D) {
            setVelocity(new PVector(4, velocity.y));
        }

        if (key == KeyEvent.VK_W) {
            setVelocity(new PVector(velocity.x, -4));
        }

        if (key == KeyEvent.VK_S) {
            setVelocity(new PVector(velocity.x, 4));
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
            if (velocity.x < 0) {
                velocity.x = 0;
            }
        }

        if (key == KeyEvent.VK_D) {
            // If the current speed is headed right, clear it.  dont otherwise it interferes with another key press.
            if (velocity.x > 0) {
                velocity.x = 0;
            }
        }

        if (key == KeyEvent.VK_W) {
            // If the current speed is headed up, clear it.  dont otherwise it interferes with another key press.
            if (velocity.y < 0) {
                velocity.y = 0;
            }
        }

        if (key == KeyEvent.VK_S) {
            // If the current speed is headed down, clear it.  dont otherwise it interferes with another key press.
            if (velocity.y > 0) {
                velocity.y = 0;
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
        return velocity.y != 0 || velocity.x != 0;
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

    public int getCurrentShield() {
        return currentShield;
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

