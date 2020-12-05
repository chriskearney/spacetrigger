package com.comandante.spacetrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelOne extends Level {

    public LevelOne() {

        //1 - Second, Aka Start of the game:

        List<Alien> secondOneAliens = new ArrayList<>();

        secondOneAliens.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT, 528, -214));
        secondOneAliens.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT,464, -154));
        secondOneAliens.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT,400, -94));

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 100, -35 * i);
            secondOneAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 170, -35 * i);
            secondOneAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 250, -35 * i);
            secondOneAliens.add(alienNymph);
        }

        alienTimeMap.put(3L, secondOneAliens);


        //10 - Seconds

        List<Alien> secondTenAliens = new ArrayList<>();

        secondTenAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,50, -214));
        secondTenAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,114, -154));
        secondTenAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,178, -94));

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 100, -35 * i);
            secondTenAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 170, -35 * i);
            secondTenAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 250, -35 * i);
            secondTenAliens.add(alienNymph);
        }


        alienTimeMap.put(15L, secondTenAliens);

        //30 - Seconds

        List<Alien> thirtySecondAliens = new ArrayList<>();

        thirtySecondAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,50, -214));
        thirtySecondAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,114, -154));
        thirtySecondAliens.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,178, -94));

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 100, -35 * i);
            thirtySecondAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 170, -35 * i);
            thirtySecondAliens.add(alienNymph);
        }

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 250, -35 * i);
            thirtySecondAliens.add(alienNymph);
        }

        alienTimeMap.put(30L, thirtySecondAliens);

    }
}
