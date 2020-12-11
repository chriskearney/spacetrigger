package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Sprite;
import com.google.common.collect.Lists;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayerHealthBar extends Sprite {

    private int pctFull;
    private final BufferedImage playerHealthBarFrame = Assets.PLAYER_HEALTH_BAR_FRAME;
    private final BufferedImage playerHealthBarEmpty = Assets.PLAYER_HEALTH_BAR_EMPTY;
    private final BufferedImage playerHealthBarFull = Assets.PLAYER_HEALTH_BAR_FULL;

    private final static double TOTAL_HEALTH_BARS = 56;

    private List<Boolean> healthBars;

    public PlayerHealthBar(int x, int y, int pctFull) {
        super(x, y, 0);
        this.pctFull = pctFull;
        calculateHealthBars(pctFull);
    }

    private void calculateHealthBars(int healthAmount) {
        //(12 * 4) + 4 (black line seperators)
        // 48 lines, every 12, there's a 1x16 blank
        double perBarHealthAmount = 100 / TOTAL_HEALTH_BARS;
        double numberOfFullBars = Math.round(healthAmount / perBarHealthAmount);
        double numberOfEmptyBars = TOTAL_HEALTH_BARS - numberOfFullBars;
        healthBars = Lists.newArrayList();
        for (int i = 0; i < numberOfFullBars; i++) {
            healthBars.add(true);
        }
        for (int i = 0; i < numberOfEmptyBars; i++) {
            healthBars.add(false);
        }
    }
    
    public BufferedImage drawHealthBar() {
        BufferedImage healthBar = new BufferedImage(playerHealthBarFrame.getWidth(), playerHealthBarFrame.getHeight(), playerHealthBarFrame.getType());
        Graphics graphics = healthBar.getGraphics();
        int xPos = 2;
        for (int i = 0; i < healthBars.size(); i++) {
            if (i == 12 || i == 25 || i == 38) {
                xPos++;
            }
            Boolean healthBarLineStatus = healthBars.get(i);
            BufferedImage lineToDraw = null;
            if (healthBarLineStatus) {
                lineToDraw = playerHealthBarFull;
            } else {
                lineToDraw = playerHealthBarEmpty;
            }
            graphics.drawImage(lineToDraw, xPos, 2, null);
            xPos++;
        }
        graphics.drawImage(playerHealthBarFrame, 0, 0, null);
        graphics.dispose();
        return healthBar;
    }

    public static void main(String[] args) {
        PlayerHealthBar playerHealthBar = new PlayerHealthBar(0, 0, 34);
        BufferedImage image = playerHealthBar.drawHealthBar();
        System.out.println("hi");
    }
}
