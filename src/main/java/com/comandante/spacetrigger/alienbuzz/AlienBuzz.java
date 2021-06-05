package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;
import com.comandante.spacetrigger.aliennymph.AlienNymphBullet;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Optional;
import java.util.Random;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class AlienBuzz extends Alien {

    public AlienBuzz(PVector location) {
        super(location, 200, .03);
        setVelocity(new PVector(.1, .2));
        setAcceleration(new PVector(0, 0));
    }

    @Override
    protected void initAlien() {
        loadWarpAnimation(Assets.getAlienBuzzWarpAnimation());
        loadSpriteSheetAnimation(Assets.getAlienBuzzAnimation());
        loadExplosion(Assets.getAlienBuzzExplosion());
    }

    public AlienBuzz(PVector location, int hitPoints, double speed) {

        super(location, hitPoints, speed);
    }

    @Override
    public void move() {
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        super.move();

        Optional<PVector> vectorToPlayerShipOptional = getVectorToPlayerShip();
        if (!vectorToPlayerShipOptional.isPresent()) {
            return;
        }

        PVector vectorToPlayerShip = vectorToPlayerShipOptional.get();
        double mag = vectorToPlayerShip.mag();

        if (mag < 500) {
            double randoPercent = random.nextDouble(100);
            if (randoPercent < .3) {
                fire();
            }
        }

        if (mag < 100) {
            PVector pVector = vectorToPlayerShip.get();
            pVector.normalize();
            pVector.mult(-4);
            applyForce(pVector );
        } else {
            vectorToPlayerShip.normalize();
            vectorToPlayerShip.mult(0.1);
            applyForce(vectorToPlayerShip);
            PVector random = PVector.random2D();
            random.mult(.4);
            applyForce(random);
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
    }


    public void fire() {
        if (projectiles.size() > 0) {
            return;
        }
        PVector whereToFireFrom = new PVector((location.x + width / 2), (location.y + height / 2));
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
