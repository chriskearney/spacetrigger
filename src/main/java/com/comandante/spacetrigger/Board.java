package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.player.PlayerStatusBars;
import com.comandante.spacetrigger.player.PlayerShip;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class Board extends JPanel implements ActionListener {

    private final int DELAY = 10;
    private Timer timer;
    private PlayerShip playerShip;
    private boolean ingame;

    private long startTime;
    private Level currentLevel;

    private ArrayList<Alien> aliens;
    private ArrayList<Drop> drops;

    private BufferedImage background_3;
    private BufferedImage background_2;
    private BufferedImage background_1;

    private double yOffset_3 = 0;
    private double yDelta_3 = .9;
    private final SplittableRandom random = new SplittableRandom();

    private final EventBus eventBus;

    private int ticks;

    private double yOffset_2 = 0;
    private double yDelta_2 = 1;

    private double yOffset_1 = 0;
    private double yDelta_1 = 1.4;

    private PlayerStatusBars playerStatusBars;

    public Board() {
        this.eventBus = new EventBus();
        this.initBoard();
    }

    private void initBoard() {
        this.addKeyListener(new BoardKeyAdapter());
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.resetBoard();
        this.timer = new Timer(DELAY, this);
        this.timer.start();
        this.background_1 = Assets.BOARD_BACKGROUND_1;
        this.background_2 = Assets.BOARD_BACKGROUND_2;
        this.background_3 = Assets.BOARD_BACKGROUND_3;
    }

    private void resetBoard() {
        this.ingame = true;
        if (playerShip != null) {
            eventBus.unregister(playerShip);
        }
        if (playerStatusBars != null) {
            eventBus.unregister(playerStatusBars);
        }
        this.playerShip = new PlayerShip(eventBus);
        this.playerStatusBars = new PlayerStatusBars(new PVector(0, 0));
        this.eventBus.register(playerStatusBars);
        this.eventBus.register(playerShip);
        initAliens();
        initDrops();
        this.startTime = System.currentTimeMillis();
        this.currentLevel = new LevelOne(eventBus);
    }

    public void initAliens() {
        aliens = Lists.newArrayList();
    }

    public void initDrops() {
        drops = Lists.newArrayList();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgrounds(g);
        if (ingame) {
            drawStatusBars((Graphics2D) g);
            drawAliens((Graphics2D) g);
            drawPlayerShip((Graphics2D) g);
            drawDrops((Graphics2D) g);
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawStatusBars(Graphics2D g) {
        Sprite.SpriteRender spriteRender = playerStatusBars.getSpriteRender();
        AffineTransform t = getAffineTransform(spriteRender.getX(), spriteRender.getY());// x/y set here, ball.x/y = double, ie: 10.33
        g.drawImage(spriteRender.getImage(), t, null);
    }

    private void drawDamageAnimations(Graphics2D g, Sprite sprite) {
        if (sprite.isExploding() || !sprite.isVisible()) {
            return;
        }
        for (int i = 0; i < sprite.getDamageAnimations().size(); i++) {
            SpriteSheetAnimation spriteDamageAnimations = sprite.getDamageAnimations().get(i);
            Point2D renderPoint = spriteDamageAnimations.getRenderPoint().get();

            Optional<BufferedImage> currentFrame = spriteDamageAnimations.updateAnimation();
            if (currentFrame.isPresent()) {
                // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                AffineTransform t = getAffineTransform((sprite.getX() + renderPoint.getX()) - currentFrame.get().getWidth() / 2, (sprite.getY() + renderPoint.getY()) - currentFrame.get().getHeight() / 2);// x/y set here, ball.x/y = double, ie: 10.33
                g.drawImage(currentFrame.get(), t, this);
            } else {
                sprite.getDamageAnimations().remove(i);
            }
        }
    }

    private static AffineTransform getAffineTransform(double x, double y) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y); // x/y set here, ball.x/y = double, ie: 10.33
        t.scale(1, 1); // scale = 1
        return t;
    }

    private void drawDrops(Graphics2D g) {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Sprite.SpriteRender spriteRender = drop.getSpriteRender();
            AffineTransform t = getAffineTransform(spriteRender.getX(), spriteRender.getY()); // x/y set here, ball.x/y = double, ie: 10.33
            g.drawImage(spriteRender.getImage(), t, this);
        }
    }

    private void drawPlayerShip(Graphics2D g) {
        if (playerShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = playerShip.getSpriteRender();
            AffineTransform t = getAffineTransform(spaceShipRender.getX(), spaceShipRender.getY());
            g.drawImage(spaceShipRender.getImage(), t, this);
            if (playerShip.getShield().isVisible() && !playerShip.isExploding()) {
                Sprite shield = playerShip.getShield();
                PVector v = new PVector((spaceShipRender.getX() + (playerShip.getWidth() / 2) - (playerShip.getShield().getWidth() / 2)),
                        (spaceShipRender.getY() + (playerShip.getHeight() / 2) - (playerShip.getShield().getHeight() / 2)));
                shield.setOriginalLocation(v);
                Sprite.SpriteRender spriteRender = shield.getSpriteRender();
                AffineTransform transform = getAffineTransform(spriteRender.getX(), spriteRender.getY());
                g.drawImage(spriteRender.getImage(), transform, this);
                drawDamageAnimations(g, playerShip.getShield());
            }
            drawDamageAnimations(g, playerShip);
            if (playerShip.isMovement() && !playerShip.isExploding()) {
                SpriteSheetAnimation exhaust = playerShip.getExhaust();
                Optional<BufferedImage> currentFrame = exhaust.updateAnimation();
                if (currentFrame.isPresent()) {
                    g.drawImage(currentFrame.get(), getAffineTransform(spaceShipRender.getX() + (playerShip.getWidth() / 2) + 8, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11), this);
                    g.drawImage(currentFrame.get(), getAffineTransform(spaceShipRender.getX() + (playerShip.getWidth() / 2) - 16, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11), this);
                }
            }
        }
        List<Projectile> space = playerShip.getMissiles();
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).isVisible()) {
                Sprite.SpriteRender spaceShipMissleRender = space.get(i).getSpriteRender();
                g.drawImage(spaceShipMissleRender.getImage(), getAffineTransform(spaceShipMissleRender.getX(), spaceShipMissleRender.getY()), this);
            }
        }
    }

    private void drawAliens(Graphics2D g) {
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.isVisible()) {
                Sprite.SpriteRender aliensRender = alien.getSpriteRender();
                g.drawImage(aliensRender.getImage(), getAffineTransform(aliensRender.getX(), aliensRender.getY()), this);

                // Alien projectiles
                for (int j = 0; j < alien.getMissiles().size(); j++) {
                    Sprite.SpriteRender alienMissleRender = alien.getMissiles().get(j).getSpriteRender();
                    AffineTransform backup = g.getTransform();
                    //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
                    //is the angle to rotate the image. If you want to rotate around the center of an image,
                    //use the image's center x and y coordinates for rx and ry.
                    AffineTransform affineTransform = getAffineTransform(alienMissleRender.getX(), alienMissleRender.getY());
                    //System.out.println("using heading: " + Math.toDegrees(alienMissleRender.getHeading()));
                    affineTransform.rotate(alienMissleRender.getHeading());
                    //Set our Graphics2D object to the transform
                    g.transform(affineTransform);
                    g.drawImage(alienMissleRender.getImage(), 0, 0, null );
                    g.setTransform(backup);
                }
                if (alien.isExploding()) {
                    continue;
                }

                // Damage Animations
                drawDamageAnimations(g, alien);
            }
        }
    }

    private void drawGameOver(Graphics g) {

        String msg = "Game Over - Press \"R\" to restart";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_X - fm.stringWidth(msg)) / 2, BOARD_Y / 2);
    }

    private static void positionBackground(Graphics2D g2d, BufferedImage bg, double yOffset) {
        if (bg != null) {
            int xPos = (BOARD_X - bg.getWidth()) / 2;
            int yPos = (int) Math.round(yOffset);

            while (yPos > 0) {
                yPos -= bg.getHeight();
                g2d.drawImage(bg, xPos, yPos, null);
            }

            yPos = (int) Math.round(yOffset);
            while (yPos < BOARD_Y) {
                g2d.drawImage(bg, xPos, yPos, null);
                yPos += bg.getHeight();
            }
        }
    }

    private static double incrementOffset(double offSet, double delta, int maxHeight) {
        offSet += delta;
        if (offSet > maxHeight) {
            offSet = 0;
        }
        return offSet;
    }

    private void drawBackgrounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        yOffset_3 = incrementOffset(yOffset_3, yDelta_3, background_3.getHeight());
        yOffset_2 = incrementOffset(yOffset_2, yDelta_2, background_2.getHeight());
        yOffset_1 = incrementOffset(yOffset_1, yDelta_1, background_1.getHeight());

        positionBackground(g2d, background_3, yOffset_3);
        positionBackground(g2d, background_2, yOffset_2);
        positionBackground(g2d, background_1, yOffset_1);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateMissiles();
        updateSpaceShip();
        updateAliens();
        updateDrops();
        checkCollisions();
        repaint();
    }

    public void checkCollisions() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).isCollison(playerShip).isPresent()) {
                playerShip.setExploding(true, true);
                aliens.get(i).setExploding(true, true);
            }

            List<Projectile> projectiles = aliens.get(i).getMissiles();
            for (int j = 0; j < projectiles.size(); j++) {
                Projectile projectile = projectiles.get(j);
                if (playerShip.isShield()) {
                    playerShip.getShield().setVisible(true);
                    Optional<Point2D> shieldCollision = projectile.isCollison(playerShip.getShield(), 100);
                    if (shieldCollision.isPresent()) {
                        projectile.setVisible(false);
                        playerShip.getShield().addDamageAnimation(projectile, shieldCollision.get());
                    }
                } else {
                    Optional<Point2D> collison = projectile.isCollison(playerShip);
                    if (collison.isPresent()) {
                        int newHitPointsPct = playerShip.calculateHitPointsPercentAfterDamageApplied(projectile, collison.get());
                        projectile.setVisible(false);
                        if (newHitPointsPct == 0) {
                            playerShip.setExploding(true, true);
                        }
                        eventBus.post(new PlayerShipHealthUpdateEvent(newHitPointsPct));
                    }
                }
            }
        }

        List<Projectile> spaceShipMissles = playerShip.getMissiles();
        boolean newExplosion = false;
        for (int i = 0; i < spaceShipMissles.size(); i++) {
            for (int j = 0; j < aliens.size(); j++) {
                Optional<Point2D> collisonPoint = aliens.get(j).isCollison(spaceShipMissles.get(i));
                if (collisonPoint.isPresent()) {
                    int newHitPointsPercentage = aliens.get(j).calculateHitPointsPercentAfterDamageApplied(spaceShipMissles.get(i), collisonPoint.get());
                    spaceShipMissles.get(i).setVisible(false);
                    if (newHitPointsPercentage <= 0) {
                        processDrops(aliens.get(j));
                        aliens.get(j).setExploding(true, true);
                        newExplosion = true;
                    }
                }
            }
        }

        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Optional<Point2D> collison = drop.isCollison(playerShip);
            if (collison.isPresent()) {
                eventBus.post(drop.getEvent());
                drop.setVisible(false);
            }
        }

        if (newExplosion) {
            aliens.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
        }
    }

    private void processDrops(Alien alien) {
        int rnd = random.nextInt(0, 100);
        for (int i = 0; i < alien.getDrops().size(); i++) {
            Drop drop = alien.getDrops().get(i);
            int dropPercent = drop.getDropRate().getPercent();
            if (rnd < dropPercent) {
                drop.setOriginalLocation(new PVector(alien.getX() + (alien.getWidth() / 2) - (drop.getWidth() / 2),
                        alien.getY() + (alien.getHeight() / 2) - (drop.getHeight() / 2)));
                drop.setVisible(true);
                drops.add(drop);
                return;
            }
        }
    }


    private void updateMissiles() {

        List<Projectile> spaceShipProjectiles = playerShip.getMissiles();

        for (int i = 0; i < spaceShipProjectiles.size(); i++) {
            Projectile projectile = spaceShipProjectiles.get(i);
            if (projectile.isVisible()) {
                projectile.move();
            }
        }

        spaceShipProjectiles.removeIf(s -> !s.isVisible());

        for (int i = 0; i < aliens.size(); i++) {
            boolean remove = false;
            for (int j = 0; j < aliens.get(i).getMissiles().size(); j++) {
                Projectile alienMissle = aliens.get(i).getMissiles().get(j);
                if (alienMissle.isVisible()) {
                    alienMissle.move();
                } else {
                    remove = true;
                }
            }
            if (remove) {
                aliens.get(i).getMissiles().removeIf(missle -> !missle.isVisible());
            }
        }


    }

    private void updateSpaceShip() {
        if (!playerShip.isVisible() && !playerShip.isExploding()) {
            ingame = false;
        }
        playerShip.move();
    }

    private void updateAliens() {
        aliens.addAll(currentLevel.getAlien(System.currentTimeMillis() - startTime));

        if (currentLevel.isEmpty() && aliens.isEmpty()) {
            ingame = false;
            return;
        }

        boolean remove = false;
        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            if (a.isVisible()) {
                a.move();
            } else {
                remove = true;
            }
        }

        if (remove) {
            Iterator<Alien> iterator = aliens.iterator();
            while (iterator.hasNext()) {
                Alien next = iterator.next();
                if (!next.isVisible()) {
                    eventBus.unregister(next);
                    iterator.remove();
                }
            }
        }
    }

    private void updateDrops() {
        boolean remove = false;
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            if (drop.isVisible()) {
                drop.move();
            } else {
                remove = true;
            }
        }

        if (remove) {
            Iterator<Drop> iterator = drops.iterator();
            while (iterator.hasNext()) {
                Drop next = iterator.next();
                iterator.remove();
            }
        }
    }


    private class BoardKeyAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            playerShip.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!ingame && e.getKeyCode() == KeyEvent.VK_R) {
                resetBoard();
            } else {
                playerShip.keyPressed(e);
            }
        }
    }
}