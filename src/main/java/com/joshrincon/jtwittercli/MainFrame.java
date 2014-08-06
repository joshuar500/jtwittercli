package com.joshrincon.jtwittercli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class MainFrame extends JFrame {

    ConsolePanel consolePanel;
    private JScrollPane scrollPane;
    private JScrollBar scrollBar;

    public MainFrame() {
        super("JTwitter CLI");

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        consolePanel = new ConsolePanel();
        scrollPane = new JScrollPane(consolePanel);

        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBounds(0, 40, 600, 400);
        scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());

        add(scrollPane, BorderLayout.CENTER);

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

}
