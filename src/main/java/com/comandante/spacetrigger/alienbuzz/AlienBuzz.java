package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.*;
import com.comandante.spacetrigger.aliennymph.AlienNymphBullet;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Optional;
import java.util.Random;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class AlienBuzz extends Alien {

    public AlienBuzz(PVector location) {
        super(location, 200);
        velocity.add(new PVector(.1, .2));
    }

    @Override
    protected void initAlien() {
        loadWarpAnimation(Assets.getAlienBuzzWarpAnimation());
        loadSpriteSheetAnimation(Assets.getAlienBuzzAnimation());
        loadExplosion(Assets.getAlienBuzzExplosion());
    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public double getRandomNumberUsingNextDouble(double min, double max) {
        return random.nextDouble(max - min) + min;
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
            } else if (getRandomNumberUsingNextInt(0, 100) < 60)  {
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

    @Override
    public SpriteRender getSpriteRender() {
        SpriteRender spriteRender = super.getSpriteRender();
        PVector v = velocity.get();
        v.normalize();
        v.mult(.1);
        image = GfxUtil.rotateImageByDegrees(spriteRender.getImage(), v.heading());
        return new SpriteRender(spriteRender.getLocation(), image);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
