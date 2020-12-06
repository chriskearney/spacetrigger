package com.comandante.spacetrigger;

import com.comandante.spacetrigger.aliennymph.AlienNymph;
import com.comandante.spacetrigger.alienscout.AlienScout;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import static com.comandante.spacetrigger.Main.BOARD_X;

public class LevelOne extends Level {

    private final SplittableRandom random = new SplittableRandom();

    private AlienNymph configureAlienRogue(int pause, AlienNymph alienNymph) {
        alienNymph.pause(3 + pause);
        alienNymph.addDownAnglePath(1, .9, 1200, Sprite.DownAnglePathDirection.LEFT_TO_RIGHT);
        alienNymph.addPoint(BOARD_X - 300, 200);
        return alienNymph;
    }

    private AlienScout configureAlienScout(AlienScout alienScout) {
        alienScout.pause(3);
        alienScout.addDownAnglePath(3, .3, 1200, Sprite.DownAnglePathDirection.LEFT_TO_RIGHT);
        alienScout.pause(1);
        return alienScout;
    }

    public LevelOne() {

        //1 - Second, Aka Start of the game:

        List<Alien> roundOne = new ArrayList<>();


        for (int i = 0; i < 3; i++) {
            roundOne.add(configureAlienScout(new AlienScout(80 * i, 115)));
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(i, new AlienNymph(i * 40, 70));
            AlienNymph alienNymph2 = configureAlienRogue(i, new AlienNymph(i * 40, 20));
            roundOne.add(alienNymph);
            roundOne.add(alienNymph2);
        }



        alienTimeMap.put(1L, roundOne);
    }
}
