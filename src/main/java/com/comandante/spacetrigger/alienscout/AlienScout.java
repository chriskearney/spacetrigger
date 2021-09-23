package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.google.common.eventbus.EventBus;

import java.util.Optional;

public class AlienScout extends Alien {

    private int scoutTicks;
    private final EventBus eventBus;

    public AlienScout(PVector location, EventBus eventBus) {
        super(location,
                150,
                Optional.of(Assets.ALIEN_SCOUT),
                Optional.empty(),
                Optional.of(Assets.getAlienScoutExplosionAnimation()),
                Optional.of(Assets.getAlientScoutWarpAnimation()));
        this.eventBus = eventBus;
       // velocity.add(new PVector(4, 0));
    }

    public void fire() {
        PVector sub = PVector.sub(shipLocation, location);
        sub.normalize();
        sub.mult(0.1);
        AlienScoutMissle alienScoutMissle = new AlienScoutMissle((location.x + getWidth() / 2) - 7, (location.y + getHeight() / 2) + 40, sub);
        eventBus.register(alienScoutMissle);
        projectiles.add(alienScoutMissle);
    }

    public void update() {
        try {
            if (isExploding || warpAnimation.isPresent()) {
                return;
            }
            int stepSize = 100;
            if (scoutTicks % stepSize == 0) {
                int randoPercent = random.nextInt(100);
                if (randoPercent < 90) {
                    if (isShipInViewAngle(velocity.heading(), PVector.radians(60))) {
                        fire();
                    }
                }
            }

            Optional<PVector> vectorToPlayerShip = getVectorToPlayerShip();
            if (!vectorToPlayerShip.isPresent()) {
                return;
            }

            double lengthOfVectorToShip = vectorToPlayerShip.get().mag();
            PVector pVector = vectorToPlayerShip.get().get();
            pVector.normalize();
            pVector.mult(.03);
            if (lengthOfVectorToShip <= 170) {
                pVector.mult(.1);
                pVector.mult(-1);
            }

            applyForce(pVector);
            applyForce(new PVector(0, 1));

            velocity.add(acceleration);
            velocity.limit(.2);
            location.add(velocity);
            acceleration.mult(0);

            scoutTicks++;
        } finally {
            super.update();
        }
    }
}
