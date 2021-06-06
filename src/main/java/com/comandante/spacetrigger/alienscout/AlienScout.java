package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;
import com.google.common.eventbus.EventBus;

import java.util.Optional;

public class AlienScout extends Alien {

    private int scoutTicks;
    private final EventBus eventBus;

    public AlienScout(PVector location, EventBus eventBus) {
        super(location, 1000);
        this.eventBus = eventBus;
        velocity.add(new PVector(.1, 0));
    }

     protected void initAlien() {
        loadImage(Assets.ALIEN_SCOUT);
        loadExplosion(Assets.getAlienScoutExplosionAnimation());
        loadWarpAnimation(Assets.getAlientScoutWarpAnimation());
    }

    public void fire() {
        PVector sub = PVector.sub(shipLocation, location);
        sub.normalize();
        sub.mult(0.1);
        AlienScoutMissle alienScoutMissle = new AlienScoutMissle((location.x + getWidth() / 2) - 7, (location.y + getHeight() / 2) + 40, sub);
        eventBus.register(alienScoutMissle);
        projectiles.add(alienScoutMissle);
    }

    public void move() {
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        int stepSize = 100;
        if (scoutTicks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent < 16) {
                fire();
            }
        }

        Optional<PVector> vectorToPlayerShip = getVectorToPlayerShip();
        if (!vectorToPlayerShip.isPresent()) {
            return;
        }
        vectorToPlayerShip.get().normalize();
        vectorToPlayerShip.get().mult(.1);
        location.add(vectorToPlayerShip.get());

        scoutTicks++;
    }
}
