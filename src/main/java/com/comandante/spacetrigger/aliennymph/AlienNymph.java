package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;

import java.util.Optional;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;
import static com.comandante.spacetrigger.PVector.cos;
import static com.comandante.spacetrigger.PVector.radians;

public class AlienNymph extends Alien {

    private int nymphTicks = 0;

    public AlienNymph(PVector location) {
        super(location,
                400,
                Optional.of(Assets.ALIEN_NYMPH),
                Optional.empty(),
                Optional.of(Assets.getAlienNymphExplosion()),
                Optional.of(Assets.getAlienNymphWarpAnimation()));
        applyForce(new PVector(3, 0));

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

            velocity.add(acceleration);

            if ((location.x > BOARD_X) || (location.x < 0)) {
                velocity.x = velocity.x * -1;
            }
            if ((location.y > BOARD_Y) || (location.y < 0)) {
                velocity.y = velocity.y * -1;
            }

            location.add(velocity);
            velocity.limit(2);
            acceleration.mult(0);

            int stepSize = 50;
            if (nymphTicks % stepSize == 0) {
                if (isShipInViewAngle(radians(90), radians(10))) {
                    fire();
                }
            }
            nymphTicks++;

        } finally {
            super.update();
        }
    }
}
