package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.*;

import java.util.Optional;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;
import static com.comandante.spacetrigger.PVector.radians;

public class AlienBuzz extends Alien {

    public AlienBuzz(PVector location) {
        super(location,
                20,
                Optional.empty(),
                Optional.of(Assets.getAlienBuzzAnimation()),
                Optional.of(Assets.getAlienBuzzExplosion()),
                Optional.of(Assets.getAlienBuzzWarpAnimation()));

        velocity.add(new PVector(.1, .2));
    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public double getRandomNumberUsingNextDouble(double min, double max) {
        return random.nextDouble(max - min) + min;
    }

    @Override
    public void update() {
        try {
            if (isExploding || warpAnimation.isPresent()) {
                return;
            }

            Optional<PVector> vectorToPlayerShipOptional = getVectorToPlayerShip();
            if (!vectorToPlayerShipOptional.isPresent()) {
                return;
            }

            PVector vectorToPlayerShip = vectorToPlayerShipOptional.get();
            double mag = vectorToPlayerShip.mag();

            if (mag < 300) {
                if (isShipInViewAngle(velocity.heading(), radians(10))) {
                    fire();
                }
            }

            if (mag < getRandomNumberUsingNextInt(60, 70)) {
                PVector pVector = vectorToPlayerShip.get();
                pVector.normalize();
                pVector.mult(-4);
                applyForce(pVector);
                PVector random = PVector.random2D();
                random.mult(.4);
                applyForce(random);
            } else {
                if (getRandomNumberUsingNextInt(0, 100) < 10) {
                    PVector random = PVector.random2D();
                    random.normalize();
                    random.mult(.1);
                    vectorToPlayerShip.normalize();
                    vectorToPlayerShip.mult(0.1);
                    applyForce(vectorToPlayerShip);
                    applyForce(random);
                } else if (getRandomNumberUsingNextInt(0, 100) < 60) {
                    vectorToPlayerShip.normalize();
                    vectorToPlayerShip.mult(0.1);
                    applyForce(vectorToPlayerShip);
                }
            }

            velocity.add(acceleration);
            location.add(velocity);
            velocity.limit(2);
            acceleration.mult(0);

            if ((location.x > BOARD_X) || (location.x < 0)) {
                velocity.x = velocity.x * -1;
            }
            if ((location.y > BOARD_Y) || (location.y < 0)) {
                velocity.y = velocity.y * -1;
            }

        } finally {
            PVector v = velocity.get();
            v.normalize();
            v.mult(.1);
            super.update(Optional.of(GfxUtil.round(v.heading(), 5)));
        }
    }


    public void fire() {
        if (projectiles.size() > 0) {
            return;
        }
        PVector whereToFireFrom = new PVector((location.x + getWidth() / 2), (location.y + getHeight() / 2));
        if (shipLocation != null) {
            PVector shipDirection = PVector.sub(shipLocation, location);
            shipDirection.normalize();
            shipDirection.mult(0.1);
            PVector randomNess = PVector.random2D();
            randomNess.normalize();
            randomNess.mult(.006);
            shipDirection.add(randomNess);
            projectiles.add(new AlienBuzzBullet(whereToFireFrom, shipDirection));
        }
    }

}
