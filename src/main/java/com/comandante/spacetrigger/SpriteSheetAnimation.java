package com.comandante.spacetrigger;

import com.google.common.collect.Lists;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpriteSheetAnimation {

    private final BufferedImage spriteSheet;
    private final List<BufferedImage> spriteFrames;
    private int frameTicker;
    private int frameDelay;
    private int frameNumber = 0;

    private final Optional<Point2D> renderPoint;

    private boolean looping;

    private final int x_size;
    private final int y_size;

    private Optional<BufferedImage> currentFrame;

    public SpriteSheetAnimation(int x_size,
                                int y_size,
                                int columns,
                                int rows,
                                BufferedImage spriteSheet,
                                int skipFrame,
                                int frameDelay,
                                boolean looping,
                                Optional<Point2D> renderPoint) {
        this.x_size = x_size;
        this.y_size = y_size;
        this.frameDelay = frameDelay;
        this.spriteSheet = spriteSheet;
        this.spriteFrames = Lists.newArrayList();
        this.looping = looping;
        this.renderPoint = renderPoint;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                spriteFrames.add(spriteSheet.getSubimage(i * x_size, j * y_size, x_size, y_size));
            }
        }
        for (int i = 0; i < skipFrame; i++) {
            spriteFrames.remove(i);
        }
        currentFrame = Optional.ofNullable(spriteFrames.get(0));
    }

    public SpriteSheetAnimation(int x_size, int y_size, int columns, int rows, BufferedImage spriteSheet, int skipFrame, int frameDelay, Optional<Point2D> renderPoint) {
        this(x_size, y_size, columns, rows, spriteSheet, skipFrame, frameDelay, false, renderPoint);
    }

    public SpriteSheetAnimation(int x_size, int y_size, int columns, int rows, BufferedImage spriteSheet, int skipFrame, int frameDelay) {
        this(x_size, y_size, columns, rows, spriteSheet, skipFrame, frameDelay, false, Optional.empty());
    }

    public Optional<BufferedImage> updateAnimation() {
        if (frameTicker >= frameDelay) {
            if (frameNumber >= getTotalFrames()) {
                if (looping) {
                    frameNumber = 0;
                    currentFrame = Optional.ofNullable(spriteFrames.get(0));
                } else {
                    currentFrame = Optional.empty();
                }
            } else if (frameNumber < 0) {
                currentFrame = Optional.ofNullable(spriteFrames.get(getTotalFrames() - 1));
                frameNumber = getTotalFrames() - 1;
            } else {
                currentFrame = Optional.ofNullable(spriteFrames.get(frameNumber));
                frameNumber = frameNumber + 1;
            }
            frameTicker = 0;
        } else {
            frameTicker = frameTicker + 1;
        }
        // Never reached if looping
        // When this returns Optional.empty, the SpriteSheet has completed a single loop of its frame.
        return currentFrame;
    }

    public int getTotalFrames() {
        return spriteFrames.size();
    }

    public Optional<Point2D> getRenderPoint() {
        return renderPoint;
    }

    public List<BufferedImage> getSpriteFrames() {
        return spriteFrames;
    }

    public Optional<BufferedImage> getCurrentFrame() {
        return currentFrame;
    }

    public int getX_size() {
        return x_size;
    }

    public int getY_size() {
        return y_size;
    }
}
