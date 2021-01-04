package com.comandante.spacetrigger;

import com.comandante.spacetrigger.alienbuzz.AlienBuzz;
import com.comandante.spacetrigger.aliennymph.AlienNymph;
import com.comandante.spacetrigger.alienscout.AlienScout;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.*;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class LevelOne extends Level {

    private final SplittableRandom random = new SplittableRandom();
    private final EventBus eventBus;


    public LevelOne(EventBus eventBus) {
        this.eventBus = eventBus;

        AlienBuzz aB = new AlienBuzz(0, 0);
        int alienBuzzWidth = aB.getWidth();

        List<Alien> aliens = Lists.newArrayList();
        int firstX = 0 - ((alienBuzzWidth + 8) * 5);
        for (int i = 1; i < 39; i++) {
            int alienX = 0 - ((alienBuzzWidth + 8) * i);
            int alienY = 100;
            AlienBuzz alienBuzz = new AlienBuzz(alienX, alienY);
            eventBus.register(alienBuzz);
            alienBuzz.addPoint(alienX + 300, 100);
            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.COMMON));

            alienBuzz.addPoint(alienX + 300, 690);
            aliens.add(alienBuzz);
        }
        alienTimeMap.put(1000L, aliens);

        aliens = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            AlienNymph alienNymph = new AlienNymph(i * 40 + 40, i + 2 + 20);
            eventBus.register(alienNymph);
            alienNymph.addPoint(i * 40 + 250, i + 2 + 300);
            alienNymph.addDrop(new HealthDrop(Drop.DropRate.COMMON));
            alienNymph.addDrop(new MissleDrop(Drop.DropRate.COMMON));
            aliens.add(alienNymph);
        }

        alienTimeMap.put(1500L, aliens);

        aliens = Lists.newArrayList();
        AlienScout alienScout = new AlienScout(BOARD_X - 80, 100);
        AlienScout alienScout2 = new AlienScout(BOARD_X - 150, 100);
        alienScout2.addDownAnglePath(1, .3, 600, Sprite.DownAnglePathDirection.RIGHT_TO_LEFT );
        alienScout.addDownAnglePath(1, .3, 600, Sprite.DownAnglePathDirection.RIGHT_TO_LEFT );
        eventBus.register(alienScout);
        eventBus.register(alienScout2);
        alienScout2.addDrop(new HealthDrop(Drop.DropRate.COMMON));
        alienScout.addDrop(new HealthDrop(Drop.DropRate.COMMON));

        alienScout2.addDrop(new MissleDrop(Drop.DropRate.COMMON));
        alienScout.addDrop(new MissleDrop(Drop.DropRate.COMMON));

        aliens.add(alienScout);
        aliens.add(alienScout2);
        alienTimeMap.put(4000L, aliens);




    }


}
