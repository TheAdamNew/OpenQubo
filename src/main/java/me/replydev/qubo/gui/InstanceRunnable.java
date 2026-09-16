/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import me.replydev.qubo.QuboInstance;
import me.replydev.qubo.gui.Confirm;
import me.replydev.qubo.gui.MainWindow;
import me.replydev.utils.Log;

class InstanceRunnable
implements Runnable {
    private final QuboInstance quboInstance;
    private final MainWindow window;

    public void stop() {
        this.quboInstance.stop();
    }

    public InstanceRunnable(QuboInstance quboInstance, MainWindow window) {
        this.quboInstance = quboInstance;
        this.window = window;
    }

    @Override
    public void run() {
        block2: {
            try {
                this.quboInstance.run();
            }
            catch (NumberFormatException e) {
                if (!Confirm.requestConfirm("Check threads or timeout fields and relaunch program, would you like to see an example configuration?")) break block2;
                this.window.exampleConf();
            }
        }
        this.window.idle();
        Log.logln("Stopped");
    }
}

