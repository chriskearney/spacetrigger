package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.Sprite;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Shield extends Sprite {

    public Shield() {
        super(new PVector(0, 0),
                0,
                Optional.of(Assets.PLAYER_SHIP_SHIELD),
                Optional.of(Assets.getShieldAnimation()),
                Optional.empty(),
                Optional.empty());
    }
}
