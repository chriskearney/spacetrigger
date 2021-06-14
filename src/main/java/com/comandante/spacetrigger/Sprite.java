package com.comandante.spacetrigger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.Opt;


import static com.comandante.spacetrigger.Assets.TRANSPARENT_ONE_PIXEL;
import static com.comandante.spacetrigger.Main.BOARD_X;
import static java.lang.Math.sin;

public abstract class Sprite {

    protected PVector location;
    protected PVector originalLocation;
    protected PVector velocity = new PVector(0, 0);
    protected PVector acceleration = new PVector(0, 0);
    protected int hitPoints = 0;
    protected int maxHitpoints;
    protected boolean visible;
    protected BufferedImage image;
    protected Optional<SpriteSheetAnimation> animatedImage = Optional.empty();
    protected SpriteSheetAnimation explosion;
    protected Optional<SpriteSheetAnimation> warpAnimation = Optional.empty();
    protected boolean isExploding;
    private boolean invisibleAfterExploding;

    private SpriteRender spriteRender;


    CacheLoader<ImageDegreePair, BufferedImage> loader = new CacheLoader<ImageDegreePair, BufferedImage>() {
        @Override
        public BufferedImage load(ImageDegreePair imageDegreePair) {
            return GfxUtil.rotateImageByDegrees(imageDegreePair.getBufferedImage(), imageDegreePair.getRadian());
        }
    };

    LoadingCache<ImageDegreePair, BufferedImage> rotatedImageCache = CacheBuilder.newBuilder().build(loader);

    protected double mass = 1.0;

    private long lastTickTime = 0;

    protected final SplittableRandom random = new SplittableRandom();

    protected List<SpriteSheetAnimation> damageAnimations = Lists.newArrayList();


    private double centerX;
    private double centerY;
    protected ArrayList<Point2D> trajectory = Lists.newArrayList();

    private Point2D previousAddedPoint;

    public Sprite(PVector location,
                  int hitPoints,
                  Optional<BufferedImage> image,
                  Optional<SpriteSheetAnimation> imageAnimation,
                  Optional<SpriteSheetAnimation> explosionAnimation,
                  Optional<SpriteSheetAnimation> warpAnimation) {
        this.location = location;
        this.originalLocation = new PVector(location.x, location.y);
        this.hitPoints = hitPoints;
        this.maxHitpoints = hitPoints;

        image.ifPresent(bufferedImage -> this.image = bufferedImage);
        imageAnimation.ifPresent(bufferedImage -> this.animatedImage = imageAnimation);
        explosionAnimation.ifPresent(spriteSheetAnimation -> this.explosion = spriteSheetAnimation);
        if (warpAnimation.isPresent()) {
            this.warpAnimation = warpAnimation;
        }

        calculateSpriteRender();
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    protected void loadImage(BufferedImage image) {
        this.image = image;
    }

    public void update() {
        calculateSpriteRender();
    }

    private Point2D getLastPointInTrajectory() {
        return trajectory.get(trajectory.size() - 1);
    }

    protected void loadExplosion(SpriteSheetAnimation explosion) {
        this.explosion = explosion;
    }

    protected void loadWarpAnimation(SpriteSheetAnimation animation) {
        this.warpAnimation = Optional.of(animation);
    }

    public SpriteRender getSpriteRender() {
        return spriteRender;
    }

    public void calculateSpriteRender() {

        if (!isExploding) {
            if (animatedImage.isPresent()) {
                centerX = getX() + animatedImage.get().getX_size() / 2;
                centerY = getY() + animatedImage.get().getY_size() / 2;
            } {
                centerX = getX() + getWidth() / 2;
                centerY = getY() + getHeight() / 2;
            }
        }

        if (warpAnimation.isPresent()) {
            Optional<BufferedImage> currentFrame = warpAnimation.get().updateAnimation();
            if (currentFrame.isPresent()) {
                double warpX = centerX - (currentFrame.get().getWidth() / 2);
                double warpY = centerY - (currentFrame.get().getHeight() / 2);
                this.spriteRender = new SpriteRender(new PVector(warpX, warpY), currentFrame.get());
                return;
            } else {
                warpAnimation = Optional.empty();
            }
        }


        if (isExploding) {
            Optional<BufferedImage> currentFrame = explosion.updateAnimation();
            if (currentFrame.isPresent()) {
                double explosionX = centerX - (currentFrame.get().getWidth() / 2);
                double explosionY = centerY - (currentFrame.get().getHeight() / 2);
                this.spriteRender = new SpriteRender(new PVector(explosionX, explosionY), currentFrame.get());
                return;
            }

            if (invisibleAfterExploding) {
                setVisible(false);
                isExploding = false;
            }

            this.spriteRender = new SpriteRender(location, TRANSPARENT_ONE_PIXEL);
            return;
        }

        if (animatedImage.isPresent()) {
            Optional<BufferedImage> bufferedImage = animatedImage.get().updateAnimation();
            if (!bufferedImage.isPresent()) {
                throw new RuntimeException("Need a looping animation.");
            }
            this.image = bufferedImage.get();
            this.spriteRender = new SpriteRender(location, bufferedImage.get());
            return;
        }

        this.spriteRender = new SpriteRender(location, image);
    }

    public double getX() {
        return location.x;
    }

    public double getY() {
        return location.y;
    }

    public void setOriginalLocation(PVector location) {
        this.location = location;
        this.originalLocation = location;
    }

    public int getWidth() {
        if (animatedImage.isPresent()) {
            return animatedImage.get().getX_size();
        } else {
            return image.getWidth();
        }
    }

    public int getHeight() {
        if (animatedImage.isPresent()) {
            return animatedImage.get().getY_size();
        } else {
            return image.getHeight();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(location.x, location.y, getWidth(), getHeight());
    }

    public HashSet<String> getMask() {
        // can't have any transparency to make the msak in this case
        return getMask(254);
    }

    public HashSet<String> getMask(int threshold) {
        HashSet<String> mask = new HashSet<String>();
        int pixel;

        for (int i = 0; i < (getWidth()); i++) {
            for (int j = 0; j < getHeight(); j++) {
                BufferedImage maskImage = null;
                if (animatedImage.isPresent()) {
                    maskImage = animatedImage.get().getCurrentFrame().get();
                } else {
                    maskImage = image;
                }
                pixel = maskImage.getRGB(i, j);
                if (!hasTransparency(pixel, threshold)) {
                    mask.add(((int) getX() + i) + "," + (int) (getY() + j));
                }
            }
        }
        return mask;
    }

    public static boolean hasTransparency(int pixel, int threshold) {
        int alpha = (pixel >> 24) & 0xff;
        return alpha < threshold;
    }

    public Optional<Point2D> isCollison(Sprite sprite) {
        return isCollison(sprite, 254, false);
    }

    public Optional<Point2D> isCollison(Sprite sprite, int transparencyThreshold) {
        return isCollison(sprite, transparencyThreshold, false);
    }

    public Optional<Point2D> isCollison(Sprite sprite, int transparencyThreshold, boolean overrideVisibility) {
        if ((!visible && !overrideVisibility) || isExploding || warpAnimation.isPresent()) {
            return Optional.empty();
        }
        if ((!sprite.isVisible() && !overrideVisibility) || sprite.isExploding()) {
            return Optional.empty();
        }
        Rectangle2D thisSpriteRectangle = getBounds();
        Rectangle2D incomingSpriteRectangle = sprite.getBounds();
        if (thisSpriteRectangle.intersects(incomingSpriteRectangle)) {
            HashSet<String> thisMask = getMask();
            HashSet<String> incomingMask = sprite.getMask(transparencyThreshold);
            thisMask.retainAll(incomingMask);// Check to see if any pixels in maskPlayer2 are the same as those in maskPlayer1
            if (!thisMask.isEmpty()) {
                String next = thisMask.iterator().next();
                int x = Integer.parseInt(next.split(",")[0]);
                int y = Integer.parseInt(next.split(",")[1]);
                return Optional.of(new Point2D.Double(x, y));
            }
        }
        return Optional.empty();
    }

    public void setExploding(boolean exploding, boolean invisibleAfterExploding) {
        this.isExploding = exploding;
        this.invisibleAfterExploding = invisibleAfterExploding;
    }

    public void applyForce(PVector force) {
        PVector div = PVector.div(force, mass);
        if (acceleration == null) {
            acceleration = div;
        } else {
            acceleration.add(div);
        }
    }

    public int calculateHitPointsPercentAfterHealthApplied(int amt) {
        if ((hitPoints + amt) > maxHitpoints) {
            this.hitPoints = maxHitpoints;
            return 100;
        }
        hitPoints += amt;
        float percent = (hitPoints * 100.0f) / maxHitpoints;
        return (int) Math.round(percent);
    }

    public BufferedImage cachedRotate(BufferedImage img, double radians) {
        try {
            return rotatedImageCache.get(new ImageDegreePair(img, radians));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public int calculateHitPointsPercentAfterDamageApplied(Projectile projectile, Point2D point) {
        hitPoints = hitPoints - projectile.getDamage();
        if (hitPoints <= 0) {
            return 0;
        }
        addDamageAnimation(projectile, point);
        float percent = (hitPoints * 100.0f) / maxHitpoints;
        return (int) Math.round(percent);
    }

    public void addDamageAnimation(Projectile projectile, Point2D point) {
        damageAnimations.add(projectile.getDamageAnimation(removeBoardCoords(point)));
    }

    private Point2D removeBoardCoords(Point2D point) {
        double relativeX = point.getX() - location.x;
        double relativeY = point.getY() - location.y;
        return new Point2D.Double(relativeX, relativeY);
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
        private final PVector location;
        private final BufferedImage image;

        public SpriteRender(PVector location, BufferedImage image) {
            this.location = location;
            this.image = image;
        }

        public double getX() {
            return location.x;
        }

        public double getY() {
            return location.y;
        }

        public PVector getLocation() {
            return location;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

    public static class ImageDegreePair {

        private final BufferedImage bufferedImage;
        private final double radian;

        public ImageDegreePair(BufferedImage bufferedImage, double radian) {
            this.bufferedImage = bufferedImage;
            this.radian = radian;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public double getRadian() {
            return radian;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ImageDegreePair that = (ImageDegreePair) o;
            return Double.compare(that.radian, radian) == 0 && Objects.equals(bufferedImage, that.bufferedImage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bufferedImage, radian);
        }
    }
}
