package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;

public class AlienNymph extends Alien {

    private int nymphTicks = 0;

    public AlienNymph(int x, int y) {
        super(new PVector(x, y), 400, 5);
        loadWarpAnimation(Assets.getAlienNymphWarpAnimation());
    }

    public void fire() {
        projectiles.add(new AlienNymphBullet((location.x + width / 2) - 10, (location.y + height / 2)));
    }

    @Override
    protected void initAlien() {
        loadImage(Assets.ALIEN_NYMPH);
        loadExplosion(Assets.getAlienNymphExplosion());
    }

    @Override
    public void move() {
        super.move();
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        int stepSize = 100;
        if (nymphTicks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent < 15) {
                fire();
            }
        }
        nymphTicks++;
    }
}
