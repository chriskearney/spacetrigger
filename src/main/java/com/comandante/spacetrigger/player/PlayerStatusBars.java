package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Sprite;
import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayerStatusBars extends Sprite {

    private int healthPctFull = 100;
    private int shieldPctFull = 100;
    private final BufferedImage statusBarFrame = Assets.PLAYER_HEALTH_BAR_FRAME;
    private final BufferedImage healthBarSingle = Assets.PLAYER_HEALTH_BAR_FULL;
    private final BufferedImage shieldBarSingle = Assets.PLAYER_HEALTH_BAR_FULL;

    private final static int TOTAL_BARS = 218;
    private final static int BAR_SINGLE_WIDTH = 1;

    private final List<Boolean> healthMutations = Lists.newArrayList();
    private final List<Boolean> shieldMutations = Lists.newArrayList();

    private List<Boolean> healthBars;
    private List<Boolean> shieldBars;

    int ticks = 0;

    public PlayerStatusBars(int x, int y) {
        super(x, y, 0);
        healthBars = calculateBars(healthPctFull);
        shieldBars = calculateBars(shieldPctFull);
    }

    private List<Boolean> calculateBars(int pctFull) {
        //(12 * 4) + 4 (black line seperators)
        // 48 lines, every 12, there's a 1x16 blank
        List<Boolean> bars;
        double perBarHealthAmount = 100.0f / TOTAL_BARS;
        double numberOfFullBars = Math.round(pctFull / perBarHealthAmount);
        double numberOfEmptyBars = BAR_SINGLE_WIDTH - numberOfFullBars;
        bars = Lists.newArrayList();
        for (int i = 0; i < numberOfFullBars; i++) {
            bars.add(true);
        }
        for (int i = 0; i < numberOfEmptyBars; i++) {
            bars.add(false);
        }
        return bars;
    }
    
    public BufferedImage drawStatusBars() {

        if (ticks % 3 == 0) {
            if (!healthMutations.isEmpty()) {
                Boolean aBoolean = healthMutations.remove(0);
                if (aBoolean) {
                    healthPctFull++;
                } else {
                    healthPctFull--;
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
        }

        this.healthBars = calculateBars(healthPctFull);
        this.shieldBars = calculateBars(shieldPctFull);

        BufferedImage healthBar = new BufferedImage(statusBarFrame.getWidth(), statusBarFrame.getHeight(), statusBarFrame.getType());
        Graphics graphics = healthBar.getGraphics();
        int xPos = 2;
        for (int i = 0; i < healthBars.size(); i++) {
            Boolean healthBarLineStatus = healthBars.get(i);
            if (healthBarLineStatus) {
                graphics.drawImage(healthBarSingle, xPos, 2, null);
            }
            Boolean shieldBarLineStatus = shieldBars.get(i);
            if (shieldBarLineStatus) {
                graphics.drawImage(shieldBarSingle, xPos, 16, null);
            }
            xPos += BAR_SINGLE_WIDTH;
        }
        graphics.drawImage(statusBarFrame, 0, 0, null);
        graphics.dispose();
        ticks++;
        return healthBar;
    }

    public void addHealthMutation(int amt) {
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

    public static void main(String[] args) {
        PlayerStatusBars playerStatusBars = new PlayerStatusBars(0, 0);
        BufferedImage image = playerStatusBars.drawStatusBars();
        System.out.println("hi");
    }
}
