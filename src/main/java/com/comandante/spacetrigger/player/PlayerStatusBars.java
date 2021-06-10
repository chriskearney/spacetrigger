package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.Sprite;
import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.events.PlayerShipShieldUpdateEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayerStatusBars extends Sprite {

    private int healthPctFull = 100;
    private int shieldPctFull = 100;

    private final static BufferedImage STATUS_BAR_FRAME = Assets.PLAYER_HEALTH_BAR_FRAME;
    private final static BufferedImage HEALTH_BAR_SINGLE = Assets.PLAYER_HEALTH_BAR_FULL;
    private final static BufferedImage SHIELD_BAR_SINGLE = Assets.PLAYER_SHIELD_BAR_FULL;

    private final static int TOTAL_BARS = 218;
    private final static int BAR_SINGLE_WIDTH = 1;
    private final static double PER_BAR_HEALTH_AMOUNT = 100.0f / TOTAL_BARS;

    private final List<Boolean> healthMutations = Lists.newArrayList();
    private final List<Boolean> shieldMutations = Lists.newArrayList();

    private List<Boolean> healthBars;
    private List<Boolean> shieldBars;

    int ticks = 0;

    public PlayerStatusBars(PVector location) {
        super(location, 0);
        healthBars = calculateBars(healthPctFull);
        shieldBars = calculateBars(shieldPctFull);
    }

    private List<Boolean> calculateBars(int pctFull) {
        double numberOfFullBars = Math.round(pctFull / PER_BAR_HEALTH_AMOUNT);
        double numberOfEmptyBars = TOTAL_BARS - numberOfFullBars;
        List<Boolean> bars = Lists.newArrayList();
        for (int i = 0; i < numberOfFullBars; i++) {
            bars.add(true);
        }
        for (int i = 0; i < numberOfEmptyBars; i++) {
            bars.add(false);
        }
        return bars;
    }

    @Override
    public SpriteRender getSpriteRender() {
        this.image = drawStatusBars();
        return super.getSpriteRender();
    }

    private BufferedImage drawStatusBars() {

        if (ticks % 3 == 0) {
            if (!healthMutations.isEmpty()) {
                Boolean aBoolean = healthMutations.remove(0);
                if (aBoolean) {
                    healthPctFull++;
                } else {
                    healthPctFull--;
                }
            }
        }
        if (!shieldMutations.isEmpty()) {
            Boolean aBoolean = shieldMutations.remove(0);
            if (aBoolean) {
                shieldPctFull++;
            } else {
                shieldPctFull--;
            }
        }

        this.healthBars = calculateBars(healthPctFull);
        this.shieldBars = calculateBars(shieldPctFull);

        BufferedImage healthBar = new BufferedImage(STATUS_BAR_FRAME.getWidth(), STATUS_BAR_FRAME.getHeight(), STATUS_BAR_FRAME.getType());
        Graphics graphics = healthBar.getGraphics();
        int xPos = 2;
        for (int i = 0; i < healthBars.size(); i++) {
            Boolean healthBarLineStatus = healthBars.get(i);
            if (healthBarLineStatus) {
                graphics.drawImage(HEALTH_BAR_SINGLE, xPos, 2, null);
            }
            xPos += BAR_SINGLE_WIDTH;
        }

        xPos = 2;
        for (int i = 0; i < shieldBars.size(); i++) {
            Boolean shieldBarLineStatus = shieldBars.get(i);
            if (shieldBarLineStatus) {
                graphics.drawImage(SHIELD_BAR_SINGLE, xPos, 16, null);
            }
            xPos += BAR_SINGLE_WIDTH;
        }

        graphics.drawImage(STATUS_BAR_FRAME, 0, 0, null);
        graphics.dispose();
        ticks++;
        return healthBar;
    }

    private void addHealthMutation(int amt) {
        boolean isNegative = false;
        if (amt < 0) {
            amt = -amt;
            isNegative = true;
        }
        for (int i = 0; i < amt; i++) {
            if (isNegative) {
                healthMutations.add(false);
            } else {
                healthMutations.add(true);
            }
        }
    }

    public void addShieldMutation(int amt) {
        boolean isNegative = false;
        if (amt < 0) {
            amt = -amt;
            isNegative = true;
        }
        for (int i = 0; i < amt; i++) {
            if (isNegative) {
                shieldMutations.add(false);
            } else {
                shieldMutations.add(true);
            }
        }
    }

    @Subscribe
    public void processHealthChange(PlayerShipHealthUpdateEvent playerShipHealthUpdateEvent) {
        if (playerShipHealthUpdateEvent.getNewPct() < healthPctFull) {
            int difference = healthPctFull - playerShipHealthUpdateEvent.getNewPct();
            addHealthMutation(-difference);
        } else {
            int difference = playerShipHealthUpdateEvent.getNewPct() - healthPctFull;
            addHealthMutation(difference);
        }
    }

    @Subscribe
    public void processShieldChange(PlayerShipShieldUpdateEvent playerShipShieldUpdateEvent) {
        if (playerShipShieldUpdateEvent.getNewPct() < shieldPctFull) {
            int difference = shieldPctFull - playerShipShieldUpdateEvent.getNewPct();
            addShieldMutation(-difference);
        } else {
            int difference = playerShipShieldUpdateEvent.getNewPct() - shieldPctFull;
            addShieldMutation(difference);
        }
    }
}
