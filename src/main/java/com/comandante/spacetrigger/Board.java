package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.player.PlayerStatusBars;
import com.comandante.spacetrigger.player.PlayerShip;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
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

    private ArrayList<AlienGroup> alienGroups;
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
        this.playerStatusBars = new PlayerStatusBars(0, 0);
        this.playerShip = new PlayerShip(eventBus);
        this.eventBus.register(playerStatusBars);
        this.eventBus.register(playerShip);
        initAliens();
        initDrops();
        this.startTime = System.currentTimeMillis();
        this.currentLevel = new LevelOne(eventBus);
    }

    public void initAliens() {
        alienGroups = Lists.newArrayList();
    }

    public void initDrops() {
        drops = Lists.newArrayList();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgrounds(g);
        if (ingame) {
            drawStatusBars(g);
            drawAliens(g);
            drawPlayerShip(g);
            drawDrops(g);
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawStatusBars(Graphics g) {
        Sprite.SpriteRender spriteRender = playerStatusBars.getSpriteRender();
        g.drawImage(spriteRender.getImage(), spriteRender.getX(), spriteRender.getY(), this);
    }

    private void drawDamageAnimations(Graphics g, Sprite sprite) {
        if (sprite.isExploding() || !sprite.isVisible()) {
            return;
        }
        for (int i = 0; i < sprite.getDamageAnimations().size(); i++) {
            SpriteSheetAnimation spriteDamageAnimations = sprite.getDamageAnimations().get(i);
            Point renderPoint = spriteDamageAnimations.getRenderPoint().get();

            Optional<BufferedImage> currentFrame = spriteDamageAnimations.updateAnimation();
            if (currentFrame.isPresent()) {
                // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                g.drawImage(currentFrame.get(), (sprite.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (sprite.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
            } else {
                sprite.getDamageAnimations().remove(i);
            }
        }
    }

    private void drawDrops(Graphics g) {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Sprite.SpriteRender spriteRender = drop.getSpriteRender();
            g.drawImage(spriteRender.getImage(), spriteRender.getX(), spriteRender.getY(), this);
        }
    }

    private void drawPlayerShip(Graphics g) {
        if (playerShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = playerShip.getSpriteRender();
            g.drawImage(spaceShipRender.getImage(), spaceShipRender.getX(), spaceShipRender.getY(), this);
            if (playerShip.getShield().isVisible() && !playerShip.isExploding()) {
                Sprite shield = playerShip.getShield();
                shield.setOriginalX(spaceShipRender.getX() + (playerShip.getWidth() / 2) - (playerShip.getShield().getWidth() / 2));
                shield.setOriginalY(spaceShipRender.getY() + (playerShip.getHeight() / 2) - (playerShip.getShield().getHeight() / 2));
                Sprite.SpriteRender spriteRender = shield.getSpriteRender();
                g.drawImage(spriteRender.getImage(), spriteRender.getX(), spriteRender.getY(), this);
                drawDamageAnimations(g, playerShip.getShield());
                ;
            }
            drawDamageAnimations(g, playerShip);
            if (playerShip.isMovement() && !playerShip.isExploding()) {
                SpriteSheetAnimation exhaust = playerShip.getExhaust();
                Optional<BufferedImage> currentFrame = exhaust.updateAnimation();
                if (currentFrame.isPresent()) {
                    g.drawImage(currentFrame.get(), spaceShipRender.getX() + (playerShip.getWidth() / 2) + 8, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11, this);
                    g.drawImage(currentFrame.get(), spaceShipRender.getX() + (playerShip.getWidth() / 2) - 16, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11, this);
                }
            }
        }
        List<Projectile> space = playerShip.getMissiles();
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).isVisible()) {
                Sprite.SpriteRender spaceShipMissleRender = space.get(i).getSpriteRender();
                g.drawImage(spaceShipMissleRender.getImage(), spaceShipMissleRender.getX(), spaceShipMissleRender.getY(), this);
            }
        }
    }

    private void drawAliens(Graphics g) {
        for (int i = 0; i < alienGroups.size(); i++) {
            AlienGroup alienGroup = alienGroups.get(i);
            for (int j = 0; j < alienGroup.getSize(); j++) {
                Alien alien = alienGroup.getAlien(i);
                if (alien.isVisible()) {
                    Sprite.SpriteRender aliensRender = alien.getSpriteRender();
                    g.drawImage(aliensRender.getImage(), aliensRender.getX(), aliensRender.getY(), this);

                    // Alien projectiles
                    for (int k = 0; k < alien.getMissiles().size(); k++) {
                        Sprite.SpriteRender alienMissleRender = alien.getMissiles().get(k).getSpriteRender();
                        g.drawImage(alienMissleRender.getImage(), alienMissleRender.getX(), alienMissleRender.getY(), this);
                    }

                    if (alien.isExploding()) {
                        continue;
                    }

                    // Damage Animations
                    drawDamageAnimations(g, alien);
                }
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
        boolean newExplosion = false;

        for (int i = 0; i < alienGroups.size(); i++) {
            AlienGroup alienGroup = alienGroups.get(i);
            for (int j = 0; j < alienGroup.getSize(); j++) {
                Alien alien = alienGroup.getAlien(j);

                // Check if Alien is colliding with PlayerShip.
                if (alien.isCollison(playerShip).isPresent()) {
                    playerShip.setExploding(true, true);
                    alien.setExploding(true, true);
                }

                // Check all projectiles belonging to the alien, and if they are colliding
                List<Projectile> projectiles = alien.getMissiles();
                for (int k = 0; k < projectiles.size(); k++) {
                    Projectile projectile = projectiles.get(k);
                    if (playerShip.isShield()) {
                        playerShip.getShield().setVisible(true);
                        Optional<Point> shieldCollision = projectile.isCollison(playerShip.getShield(), 100);
                        if (shieldCollision.isPresent()) {
                            projectile.setVisible(false);
                            playerShip.getShield().addDamageAnimation(projectile, shieldCollision.get());
                        }
                    } else {
                        Optional<Point> collison = projectile.isCollison(playerShip);
                        if (collison.isPresent()) {
                            int newHitPointsPct = playerShip.calculateDamage(projectile, collison.get());
                            projectile.setVisible(false);
                            if (newHitPointsPct == 0) {
                                playerShip.setExploding(true, true);
                            }
                            eventBus.post(new PlayerShipHealthUpdateEvent(newHitPointsPct));
                        }
                    }
                }

                // Check if any of the spaceship's missles are colliding with this alien
                List<Projectile> spaceShipMissles = playerShip.getMissiles();
                for (int l = 0; l < spaceShipMissles.size(); l++) {
                    Optional<Point> collisonPoint = alien.isCollison(spaceShipMissles.get(l));
                    if (collisonPoint.isPresent()) {
                        int newHitPointsPercentage = alien.calculateDamage(spaceShipMissles.get(l), collisonPoint.get());
                        spaceShipMissles.get(l).setVisible(false);
                        if (newHitPointsPercentage <= 0) {
                            processDrops(alien);
                            alien.setExploding(true, true);
                            newExplosion = true;
                        }
                    }
                }
            }
        }

        // Process drops (missles, health, etc), see if they are colliding with the playership
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Optional<Point> collison = drop.isCollison(playerShip);
            if (collison.isPresent()) {
                eventBus.post(drop.getEvent());
                drop.setVisible(false);
            }
        }
//        if (newExplosion) {
//            alienGroups.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
//        }
    }

    private void processDrops(Alien alien) {
        int rnd = random.nextInt(0, 100);
        for (int i = 0; i < alien.getDrops().size(); i++) {
            Drop drop = alien.getDrops().get(i);
            int dropPercent = drop.getDropRate().getPercent();
            if (rnd < dropPercent) {
                drop.setOriginalX(alien.getX() + (alien.getWidth() / 2) - (drop.getWidth() / 2));
                drop.setOriginalY(alien.getY() + (alien.getHeight() / 2) - (drop.getHeight() / 2));
                drop.setVisible(true);
                drops.add(drop);
            }
        }
    }


    private void updateMissiles() {

        List<Projectile> spaceShipProjectiles = playerShip.getMissiles();

        for (int i = 0; i < spaceShipProjectiles.size(); i++) {
            Projectile projectile = spaceShipProjectiles.get(i);
            if (projectile.isVisible()) {
                projectile.move();
            } else {
                spaceShipProjectiles.remove(i);
            }
        }

        for (int i = 0; i < alienGroups.size(); i++) {
            for (int j = 0; j < alienGroups.get(i).getSize(); j++) {
                Alien alien = alienGroups.get(i).getAlien(j);
                alien.getMissiles().removeIf(alienMissle -> {
                    if (alienMissle.isVisible()) {
                        alienMissle.move();
                        return false;
                    } else {
                        return true;
                    }
                });
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
        alienGroups.addAll(currentLevel.getAlien(System.currentTimeMillis() - startTime));

        if (currentLevel.isEmpty() && alienGroups.isEmpty()) {
            ingame = false;
            return;
        }

        boolean groupsNeedRemoval = false;
        for (int i = 0; i < alienGroups.size(); i++) {
            AlienGroup alienGroup = alienGroups.get(i);
            alienGroup.getAliens().removeIf(alien -> {
                if (!alien.isVisible()) {
                    eventBus.unregister(alien);
                    return true;
                } else {
                    return false;
                }
            });
            alienGroup.move();
            if (alienGroup.getAliens().isEmpty()) {
                groupsNeedRemoval = true;
            }
        }

        if (groupsNeedRemoval) {
            alienGroups.removeIf(alienGroup -> alienGroup.getAliens().isEmpty());
        }
    }

    private void updateDrops() {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            if (drop.isVisible()) {
                drop.move();
            } else {
                drops.remove(i);
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