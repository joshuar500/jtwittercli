package com.joshrincon.jtwittercli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsoleText extends JComponent {
    private String consoleIcon = ">";
    private Color consoleColor;
    private Font consoleFont;
    private JLabel consoleText;

    private Timer timer;
    private final int DELAY = 500;

    public ConsoleText() {

        consoleFont = new Font("Century Gothic", Font.PLAIN, 10);
        consoleText = new JLabel(consoleIcon);

        consoleText.setFont(consoleFont);
        consoleText.setForeground(new Color(200, 0, 100));
        consoleText.setBackground(new Color(0, 0, 0));

        //add blinking console
        timer = new Timer(DELAY, new TimerListener(consoleText));
        timer.start();
    }

    public JLabel createConsoleCursor() {
        return consoleText;
    }

    public JLabel deleteConsoleCursor() {
        return consoleText;
    }

    private class TimerListener implements ActionListener {

        private JLabel consoleText;
        private Color backGround = new Color(0,0,0);
        private Color foreGround;
        private boolean isForeground = true;

        public TimerListener(JLabel consoleText) {
            this.consoleText = consoleText;
            foreGround = consoleText.getForeground();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isForeground) {
                consoleText.setForeground(foreGround);
            } else {
                consoleText.setForeground(backGround);
            }
            isForeground = !isForeground;

        }
    }

}
