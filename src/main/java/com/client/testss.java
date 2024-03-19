package com.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class testss extends JPanel {
    public testss() {
        setFocusable(true); // Ensure that the panel can receive focus
setFocusTraversalKeysEnabled(false);//you need this.
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not used in this example
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    // Tab key pressed
                    System.out.println("Tab key pressed");
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    // Shift key pressed
                    System.out.println("Shift key EEEDpressed");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not used in this example
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KeyListener Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            testss panel = new testss();
            frame.add(panel);

            frame.setSize(300, 200);
            frame.setVisible(true);
        });
    }
}