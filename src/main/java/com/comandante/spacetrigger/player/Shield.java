package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Sprite;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Shield extends Sprite {

    public Shield() {
        super(0, 0, 0);
        initShield();
    }

    private void initShield() {
        loadImage(Assets.PLAYER_SHIP_SHIELD);
        loadSpriteSheetAnimation(new SpriteSheetAnimation(64, 64, 11, 1, Assets.PLAYER_SHIP_ANIMATED_SHIELD, 0, 3, true, Optional.empty()));
    }

}
