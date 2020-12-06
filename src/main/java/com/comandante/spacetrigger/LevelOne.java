package com.comandante.spacetrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

import static com.comandante.spacetrigger.Main.BOARD_X;

public class LevelOne extends Level {

    private final SplittableRandom random = new SplittableRandom();

    private AlienRogue configureAlienRogue(AlienRogue alienRogue) {
        alienRogue.pause(1);
        alienRogue.addDownAnglePath(.9, 1200, AlienScout.Direction.LEFT_TO_RIGHT);
        alienRogue.addPoint(100, 20);
        alienRogue.addPoint(BOARD_X - 200, 20);
        alienRogue.addDownAnglePath(.9, 1200, AlienScout.Direction.RIGHT_TO_LEFT);
        return alienRogue;
    }

    public LevelOne() {

        //1 - Second, Aka Start of the game:

        List<Alien> roundOne = new ArrayList<>();

        roundOne.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT, 528, -214));
        roundOne.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT,464, -154));
        roundOne.add(new AlienScout(AlienScout.Direction.RIGHT_TO_LEFT,400, -94));

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(300), random.nextInt(300) * i));
            roundOne.add(alienNymph);
        }

        alienTimeMap.put(3L, roundOne);

        //1 - Second, Aka Start of the game:

        List<Alien> roundTwo = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int i1 = random.nextInt(35);
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(300), random.nextInt(300) * i));
            roundTwo.add(alienNymph);
        }

        alienTimeMap.put(4L, roundTwo);

        //1 - Second, Aka Start of the game:

        List<Alien> roundThree = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(50), random.nextInt(300) * i));
            roundThree.add(alienNymph);
        }

        alienTimeMap.put(5L, roundThree);

        List<Alien> roundFour = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(50), random.nextInt(300) * i));
            roundFour.add(alienNymph);
        }

        alienTimeMap.put(6L, roundFour);

        List<Alien> roundfive = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(50), random.nextInt(300) * i));
            roundfive.add(alienNymph);
        }

        alienTimeMap.put(7L, roundfive);

        List<Alien> roundSix = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + random.nextInt(50), random.nextInt(300) * i));
            roundSix.add(alienNymph);
        }

        alienTimeMap.put(8L, roundSix);


        List<Alien> roundSeven = new ArrayList<>();

        roundSeven.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT, 328, -214));
        roundSeven.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,164, -154));
        roundSeven.add(new AlienScout(AlienScout.Direction.LEFT_TO_RIGHT,100, -94));

        alienTimeMap.put(10L, roundSeven);

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundOne.add(alienNymph);
        }

        alienTimeMap.put(3L, roundOne);

        //1 - Second, Aka Start of the game:

        roundTwo = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundTwo.add(alienNymph);
        }

        alienTimeMap.put(11L, roundTwo);

        //1 - Second, Aka Start of the game:

        roundThree = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundThree.add(alienNymph);
        }

        alienTimeMap.put(12L, roundThree);

        roundFour = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundFour.add(alienNymph);
        }

        alienTimeMap.put(13L, roundFour);

        roundfive = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundfive.add(alienNymph);
        }

        alienTimeMap.put(14L, roundfive);

        roundSix = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            AlienNymph alienNymph = configureAlienRogue(new AlienRogue(i * 40 + 10, -35 * i));
            roundSix.add(alienNymph);
        }

        alienTimeMap.put(15L, roundSix);

    }
}
