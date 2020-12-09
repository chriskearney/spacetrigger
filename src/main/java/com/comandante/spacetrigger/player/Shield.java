package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Sprite;

import java.awt.image.BufferedImage;

public class Shield extends Sprite {

    public Shield() {
        super(0, 0, 0);
        initShield();
    }

    private void initShield() {
        loadImage(Assets.PLAYER_SHIP_SHIELD);
    }

}
