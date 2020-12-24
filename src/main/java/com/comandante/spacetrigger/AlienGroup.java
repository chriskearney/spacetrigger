package com.comandante.spacetrigger;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlienGroup {

    private final List<Alien> aliens = Lists.newArrayList();
    private final GroupOrientation groupOrientation;
    private final EventBus eventBus;

    private int originalX;
    private int originalY;

    public AlienGroup(GroupOrientation groupOrientation, int originalX, int originalY, EventBus eventBus) {
        this.groupOrientation = groupOrientation;
        this.originalX = originalX;
        this.originalY = originalY;
        this.eventBus = eventBus;
    }

    public void add(Alien alien) {
        eventBus.register(alien);
        alien.setInGroup(true);
        if (aliens.isEmpty()) {
            this.aliens.add(alien);
            return;
        }

        if (groupOrientation.equals(GroupOrientation.HORIZONTAL)) {
            alien.setOriginalX(originalX - 50);
            alien.setOriginalX(originalY);
        }

        //alien.addPoint(getGroupLeader().get().getX(), getGroupLeader().get().getY());
        alien.getTrajectory().addAll(getGroupLeader().get().getTrajectory());

        aliens.add(alien);
    }

    public void move() {
        configureCurrentLeader();
        Alien alienGroupLeader = getGroupLeader().get();
        alienGroupLeader.move();

        Alien alienInFrontInLine = alienGroupLeader;
        for (int i = 1; i < aliens.size(); i++) {
            Alien alienToMove = aliens.get(i);
            Point nextMove = alienToMove.getNextMove();
            if (alienInFrontInLine.isCollidingAtBounds(new Rectangle(nextMove.getLocation().x, nextMove.getLocation().y, alienToMove.getWidth(), alienToMove.getHeight()), alienToMove.getMask()).isPresent()) {
                alienInFrontInLine = alienToMove;
                System.out.println("skip move");
                continue;
            }
            System.out.println("do move");
            alienInFrontInLine = alienToMove;
            alienToMove.move();
        }

    }

    private void configureCurrentLeader() {
        if (getGroupLeader().get().isAtEndOfTrajectory()) {
            Collections.reverse(aliens);
            for (int i = 0; i < aliens.size(); i++) {
                aliens.get(i).setReverse(true);
            }
        }
    }

    public void remove(Alien alien) {
        this.aliens.remove(alien);
    }

    public enum GroupOrientation {
        VERTICAL,
        HORIZONTAL
    }

    public Optional<Alien> getGroupLeader() {
        return Optional.ofNullable(aliens.get(0));
    }

    public int getSize() {
        return aliens.size();
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public Alien getAlien(int i) {
        return aliens.get(i);
    }
}
