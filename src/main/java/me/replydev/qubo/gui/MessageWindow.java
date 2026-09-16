/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageWindow {
    public static void showMessage(String title, String body) {
        JOptionPane.showMessageDialog(new JFrame(), body, title, 0);
    }
}

