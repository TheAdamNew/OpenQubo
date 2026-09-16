/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo;

import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.JFrame;
import me.replydev.qubo.CLI;
import me.replydev.qubo.Info;
import me.replydev.qubo.gui.MainWindow;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            FlatDarculaLaf.install();
            JFrame.setDefaultLookAndFeelDecorated(true);
            Info.gui = true;
            new MainWindow();
        } else {
            Info.gui = false;
            CLI.init(args);
        }
    }
}

