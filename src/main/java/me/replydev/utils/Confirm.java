/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.utils;

import javax.swing.JOptionPane;
import me.replydev.qubo.Info;
import me.replydev.utils.Keyboard;

public class Confirm {
    public static boolean getConfirm(String text) {
        if (Info.gui) {
            return JOptionPane.showConfirmDialog(null, text, "Confirm", 0) == 0;
        }
        return Keyboard.s(text + " (y/n): ").equalsIgnoreCase("y");
    }
}

