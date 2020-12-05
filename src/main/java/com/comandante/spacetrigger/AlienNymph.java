package com.comandante.spacetrigger;

public class AlienNymph extends Alien {

    private double speed = 0;

    public AlienNymph(int x, int y) {
        super(x, y, 75);
    }

    @Override
    protected void initAlien() {
        loadImage(Assets.ALIEN_NYMPH);
        loadExplosion(new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3));
    }

    public void fire() {
        missiles.add(new MachineGunMissle((x + width / 2) - 10, (y + height / 2) + 10, Direction.DOWN));
    }


    @Override
    public void move() {
        if (!visible) {
            return;
        }
        ticks++;

        double orbitalPeriod = 400;
        double portion = (ticks % orbitalPeriod) / orbitalPeriod; // [0, 1)
        double angle = portion * 2 * Math.PI;                    // [0, 2 * PI)

        double radius = 80;

        double planetX = originalX + radius * Math.cos(angle);
        double planetY = originalY + radius * Math.sin(angle);

        int stepSize = 100;
        if (ticks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent > 80) {
                fire();
            }
        }

        x = (int) Math.round(planetX);
        speed += .6;
        y = (int) Math.round(planetY + speed);
    }
}
