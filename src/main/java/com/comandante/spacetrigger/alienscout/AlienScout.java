package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.util.Optional;

public class AlienScout extends Alien {

    private int scoutTicks;

    public AlienScout(PVector location) {
        super(location, 1000, .0001);
        setVelocity(new PVector(.1, 0));
        setAcceleration(new PVector(0, 0));
    }

     protected void initAlien() {
        loadImage(Assets.ALIEN_SCOUT);
        loadExplosion(Assets.getAlienScoutExplosionAnimation());
        loadWarpAnimation(Assets.getAlientScoutWarpAnimation());
    }

    public void fire() {
        projectiles.add(new AlienScoutMissle((location.x + width / 2) - 7, (location.y + height / 2) + 40));
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
