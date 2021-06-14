package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;

import java.util.Optional;

public class AlienNymph extends Alien {

    private int nymphTicks = 0;

    public AlienNymph(PVector location) {
        super(location,
                400,
                Optional.of(Assets.ALIEN_NYMPH),
                Optional.empty(),
                Optional.of(Assets.getAlienNymphExplosion()),
                Optional.of(Assets.getAlienNymphWarpAnimation()));

    }

    public void fire() {
        projectiles.add(new AlienNymphBullet((location.x + getWidth() / 2) - 10, (location.y + getHeight() / 2)));
    }


    @Override
    public void update() {
        try {
            if (isExploding || warpAnimation.isPresent()) {
                return;
            }

            applyForce(PVector.random2D());

            velocity.add(acceleration);
            location.add(velocity);
            velocity.limit(2);
            acceleration.mult(0);

            int stepSize = 100;
            if (nymphTicks % stepSize == 0) {
                int randoPercent = random.nextInt(100);
                if (randoPercent < 15) {
                    fire();
                }
            }
            nymphTicks++;
        } finally {
            super.update();
        }
    }
}
