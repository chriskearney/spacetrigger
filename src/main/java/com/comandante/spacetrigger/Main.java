package com.comandante.spacetrigger;

import javax.swing.JFrame;
import java.io.IOException;

public class Main extends JFrame {

    public static final int BOARD_X = 600;
    public static final int BOARD_Y = 1080;

    public Main() {
        initUI();
    }

    private void initUI() {
        add(new Board());

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
