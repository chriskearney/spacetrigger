package com.comandante.spacetrigger;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private final MetricRegistry metricRegistry;

    public StatsPanel(LayoutManager layout, boolean isDoubleBuffered, MetricRegistry metricRegistry) {
        super(layout, isDoubleBuffered);
        this.metricRegistry = metricRegistry;

        // Config
        setSize(new Dimension(800, 800));
        setMaximumSize(new Dimension(800, 800));
        setMinimumSize(new Dimension(800, 800));

        GridLayout panelLayout = new GridLayout(0,2);

    }



}
