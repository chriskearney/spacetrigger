package com.comandante.spacetrigger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public abstract class Sprite {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int hitPoints = 0;
    protected boolean visible;
    protected BufferedImage image;
    protected SpriteSheetAnimation explosion;
    protected boolean isExploding;
    private boolean invisibleAfterExploding;

    protected List<SpriteSheetAnimation> damageAnimations = new ArrayList<>();


    private int centerX;
    private int centerY;

    private static final BufferedImage TRANSPARENT_ONE_PIXEL = createTransparentBufferedImage(1, 1);

    public Sprite(int x, int y, int hitPoints) {
        this.x = x;
        this.y = y;
        this.hitPoints = hitPoints;
    }

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void loadImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public abstract void move();

    protected void loadExplosion(SpriteSheetAnimation explosion) {
        this.explosion = explosion;
    }

    public SpriteRender getSpriteRender() {
        if (!isExploding) {
            centerX = getX() + image.getWidth() / 2;
            centerY = getY() + image.getHeight() / 2;
        }
        if (isExploding) {
            explosion.updateAnimation();
            Optional<BufferedImage> currentFrame = explosion.getCurrentFrame();
            if (currentFrame.isPresent()) {
                int explosionX = centerX - (currentFrame.get().getWidth() / 2);
                int explosionY = centerY - (currentFrame.get().getHeight() / 2);
                return new SpriteRender(explosionX, explosionY, currentFrame.get());
            }

            if (invisibleAfterExploding) {
                visible = false;
            }

            return new SpriteRender(x, y, TRANSPARENT_ONE_PIXEL);
        }
        return new SpriteRender(x, y, image);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public HashSet<String> getMask() {
        HashSet<String> mask = new HashSet<String>();
        int pixel;

        for (int i = 0; i < (getWidth()); i++) {
            for (int j = 0; j < getHeight(); j++) {
                pixel = image.getRGB(i, j);
                if (!((pixel >> 24) == 0x00)) {
                    mask.add((getX() + i) + "," + (getY() + j));
                }
            }
        }
        return mask;
    }

    public Optional<Point> isCollison(Sprite sprite) {
        if (!visible || isExploding) {
            return Optional.empty();
        }
        if (!sprite.isVisible() || sprite.isExploding()) {
            return Optional.empty();
        }
        Rectangle thisSpriteRectangle = getBounds();
        Rectangle incomingSpriteRectangle = sprite.getBounds();
        if (thisSpriteRectangle.intersects(incomingSpriteRectangle)) {
            HashSet<String> thisMask = getMask();
            HashSet<String> incomingMask = sprite.getMask();
            thisMask.retainAll(incomingMask);// Check to see if any pixels in maskPlayer2 are the same as those in maskPlayer1
            if (!thisMask.isEmpty()) {
                String next = thisMask.iterator().next();
                int x = Integer.parseInt(next.split(",")[0]);
                int y = Integer.parseInt(next.split(",")[1]);
                return Optional.of(new Point(x, y));
            }
        }
        return Optional.empty();
    }

    public void setExploding(boolean exploding, boolean invisibleAfterExploding) {
        this.isExploding = exploding;
        this.invisibleAfterExploding = invisibleAfterExploding;
    }

    public static BufferedImage createTransparentBufferedImage(int width, int height) {
        // BufferedImage is actually already transparent on my system, but that isn't
        // guaranteed across platforms.
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        // To be sure, we use clearRect, which will (unlike fillRect) totally replace
        // the current pixels with the desired color, even if it's fully transparent.
        graphics.setBackground(new Color(0, true));
        graphics.clearRect(0, 0, width, height);
        graphics.dispose();

        return bufferedImage;
    }

    public boolean calculateDamage(Missile missile, Point point) {
        hitPoints = hitPoints - missile.getDamage();
        if (hitPoints <= 0) {
            return true;
        }
        damageAnimations.add(missile.getDamageAnimation(removeBoardCoords(point)));
        return false;
    }

    private Point removeBoardCoords(Point point) {
        int relativeX = point.getLocation().x - x;
        int relativeY = point.getLocation().y - y;
        return new Point(relativeX, relativeY);
    }

    public List<SpriteSheetAnimation> getDamageAnimations() {
        return damageAnimations;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public static class SpriteRender {
        private final int x;
        private final int y;
        private final BufferedImage image;

        public SpriteRender(int x, int y, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}
