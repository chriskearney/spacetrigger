package com.comandante.spacetrigger;

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

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class Board extends JPanel implements ActionListener {

    private final int DELAY = 10;
    private Timer timer;
    private SpaceShip spaceShip;
    private BufferedImage backgroundImage;
    private boolean ingame;

    private long startTime;
    private Level currentLevel = new LevelOne();

    private ArrayList<Alien> aliens;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new BoardKeyAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        resetBoard();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void resetBoard() {
        this.backgroundImage = Assets.BOARD_BACKGROUND_1;
        ingame = true;
        spaceShip = new SpaceShip();
        initAliens();
        startTime = System.currentTimeMillis();
        currentLevel = new LevelOne();
    }

    public void initAliens() {
        aliens = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ingame) {
            drawObjects(g);
        } else {
           drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }


    private void drawObjects(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);

        if (spaceShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = spaceShip.getSpriteRender();
            g.drawImage(spaceShipRender.getImage(), spaceShipRender.getX(), spaceShipRender.getY(), this);
            for (int i = 0; i < spaceShip.getDamageAnimations().size(); i++) {
                SpriteSheetAnimation alienDamageAnimation = spaceShip.getDamageAnimations().get(i);
                Point renderPoint = alienDamageAnimation.getRenderPoint().get();
                alienDamageAnimation.updateAnimation();
                Optional<BufferedImage> currentFrame = alienDamageAnimation.getCurrentFrame();
                if (currentFrame.isPresent()) {
                    // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                    g.drawImage(currentFrame.get(), (spaceShip.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (spaceShip.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
                } else {
                    spaceShip.getDamageAnimations().remove(i);
                }
            }
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.isVisible()) {
                Sprite.SpriteRender aliensRender = alien.getSpriteRender();
                g.drawImage(aliensRender.getImage(), aliensRender.getX(), aliensRender.getY(), this);

                if (alien.isExploding) {
                    continue;
                }

                // Damage Animations
                List<SpriteSheetAnimation> damageAnimations = alien.getDamageAnimations();
                for (int j = 0; j < damageAnimations.size(); j++) {
                    SpriteSheetAnimation alienDamageAnimation = damageAnimations.get(j);
                    Point renderPoint = alienDamageAnimation.getRenderPoint().get();
                    alienDamageAnimation.updateAnimation();
                    Optional<BufferedImage> currentFrame = alienDamageAnimation.getCurrentFrame();
                    if (currentFrame.isPresent()) {
                        // Alien x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                        g.drawImage(currentFrame.get(), (alien.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (alien.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
                    } else {
                        alien.getDamageAnimations().remove(j);
                    }
                }

                // Alien projectiles
                for (int j = 0; j < alien.getMissiles().size(); j++) {
                    Sprite.SpriteRender alienMissleRender = alien.getMissiles().get(j).getSpriteRender();
                    g.drawImage(alienMissleRender.getImage(), alienMissleRender.getX(), alienMissleRender.getY(), this);
                }
            }
        }

        List<Missile> space = spaceShip.getMissiles();
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).isVisible()) {
                Sprite.SpriteRender spaceShipMissleRender = space.get(i).getSpriteRender();
                g.drawImage(spaceShipMissleRender.getImage(), spaceShipMissleRender.getX(), spaceShipMissleRender.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        String draw = "Missles: " + spaceShip.getCurrentMissles() + " | Health: " + spaceShip.getHitPoints();
        g.drawString(draw, 5, 15);
    }

    private void drawGameOver(Graphics g) {

        String msg = "Game Over - Press \"R\" to restart";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_X - fm.stringWidth(msg)) / 2, BOARD_Y / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateMissiles();
        updateSpaceShip();
        updateAliens();
        checkCollisions();
        repaint();
    }

    public void checkCollisions() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).isCollison(spaceShip).isPresent()) {
                spaceShip.setExploding(true, true);
                aliens.get(i).setExploding(true, true);
            }

            List<Missile> missiles = aliens.get(i).getMissiles();
            for (int j = 0; j < missiles.size(); j++) {
                Missile missile = missiles.get(j);
                Optional<Point> collison = missile.isCollison(spaceShip);
                if (collison.isPresent()) {
                    boolean isDoneFor = spaceShip.calculateDamage(missile, collison.get());
                    missile.setVisible(false);
                    if (isDoneFor) {
                        spaceShip.setExploding(true, true);
                    }
                }
            }
        }

        List<Missile> spaceShipMissles = spaceShip.getMissiles();
        boolean newExplosion = false;
        for (int i = 0; i < spaceShipMissles.size(); i++) {
            for (int j = 0; j < aliens.size(); j++) {
                Optional<Point> collisonPoint = aliens.get(j).isCollison(spaceShipMissles.get(i));
                if (collisonPoint.isPresent()) {
                    boolean isDoneFor = aliens.get(j).calculateDamage(spaceShipMissles.get(i), collisonPoint.get());
                    spaceShipMissles.get(i).setVisible(false);
                    if (isDoneFor) {
                        aliens.get(j).setExploding(true, true);
                        newExplosion = true;
                    }
                }
            }
        }

        if (newExplosion) {
            aliens.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
        }
    }


    private void updateMissiles() {

        List<Missile> spaceShipMissiles = spaceShip.getMissiles();

        for (int i = 0; i < spaceShipMissiles.size(); i++) {
            Missile missile = spaceShipMissiles.get(i);
            if (missile.isVisible()) {
                missile.move();
            } else {
                spaceShipMissiles.remove(i);
            }
        }

        for (int i = 0; i < aliens.size(); i++) {
            for (int j = 0; j < aliens.get(i).getMissiles().size(); j++) {
                Missile alienMissle = aliens.get(i).getMissiles().get(j);
                if (alienMissle.isVisible()) {
                    alienMissle.move();
                } else {
                    aliens.get(i).getMissiles().remove(j);
                }
            }
        }
    }

    private void updateSpaceShip() {
        spaceShip.move();
    }

    private void updateAliens() {
        aliens.addAll(currentLevel.getAlien(System.currentTimeMillis() - startTime));

        if (currentLevel.isEmpty() && aliens.isEmpty()) {
            ingame = false;
            return;
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            if (a.isVisible()) {
                a.move();
            } else {
                aliens.remove(i);
            }
        }
    }


    private class BoardKeyAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            spaceShip.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!ingame && e.getKeyCode() == KeyEvent.VK_R) {
                resetBoard();
            } else {
                spaceShip.keyPressed(e);
            }
        }
    }
}