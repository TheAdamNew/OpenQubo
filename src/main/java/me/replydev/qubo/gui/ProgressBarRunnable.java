/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import javax.swing.JProgressBar;
import me.replydev.qubo.QuboInstance;
import me.replydev.utils.Log;

class ProgressBarRunnable
implements Runnable {
    private final JProgressBar progressBar;
    private final QuboInstance quboInstance;

    public ProgressBarRunnable(JProgressBar progressBar, QuboInstance quboInstance) {
        this.progressBar = progressBar;
        this.quboInstance = quboInstance;
    }

    @Override
    public void run() {
        int percentage = (int)this.quboInstance.getPercentage();
        this.progressBar.setString(percentage + "%");
        this.progressBar.setValue(percentage);
        if (this.progressBar.getValue() == 100) {
            return;
        }
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
            Log.log_to_file(e.toString(), "log.txt");
        }
    }
}

