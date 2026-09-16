/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import javax.swing.JOptionPane;

public class Confirm {
    public static boolean requestConfirm(String text) {
        return JOptionPane.showConfirmDialog(null, text) == 0;
    }
}

