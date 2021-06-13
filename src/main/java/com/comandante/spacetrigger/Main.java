package com.comandante.spacetrigger;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import javax.swing.JFrame;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame {

    public static final int BOARD_X = 600;
    public static final int BOARD_Y = 900;

    private final MetricRegistry metrics = new MetricRegistry();

    public Main() {
        initUI();
    }

    private void initUI() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();


        reporter.start(3, TimeUnit.SECONDS);

        add(new Board(metrics));

        setTitle("SpaceTrigger");
        setSize(BOARD_X, BOARD_Y);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {

        Main main = new Main();
        main.setVisible(true);
    }
}
