package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Optional;

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

    @Override
    public void move() {
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        super.move();
        if (shipLocation != null) {
            PVector shipDirection = PVector.sub(shipLocation, location);
            shipDirection.normalize();
            shipDirection.mult(0.1);
            setAcceleration(shipDirection);
            PVector random = PVector.random2D();
            random.mult(.4);
            acceleration.add(random);
        }
        velocity.add(acceleration);
        location.add(velocity);
        velocity.limit(2);
        if ((location.x > BOARD_X) || (location.x < 0)) {
            velocity.x = velocity.x * -1;
        }
        if ((location.y > BOARD_Y) || (location.y < 0)) {
            velocity.y = velocity.y * -1;
        }
    }
}
