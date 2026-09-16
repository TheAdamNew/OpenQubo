/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.utils;

import me.replydev.qubo.CLI;
import me.replydev.utils.Keyboard;
import me.replydev.utils.Log;

public class KeyboardThread
implements Runnable {
    @Override
    public void run() {
        block16: while (true) {
            String s;
            if ((s = Keyboard.s()) == null) {
                continue;
            }
            switch (s = s.toLowerCase()) {
                case "help": {
                    System.out.println("Commands: \nstatus - show current ip\nthreads - show thread in execution\nskip - skip current scan and start the next one\nexit - exit the program");
                    continue block16;
                }
                case "status": {
                    Log.logln(CLI.getQuboInstance().getCurrent());
                    continue block16;
                }
                case "threads": {
                    Log.logln("Current threads: " + CLI.getQuboInstance().getThreads());
                    continue block16;
                }
                case "skip": {
                    Log.logln("Skipping \"" + CLI.getQuboInstance().getFilename() + "\"");
                    CLI.getQuboInstance().stop();
                    continue block16;
                }
                case "exit": {
                    if (CLI.getQuboInstance().getStartTime() != null) {
                        System.out.println(CLI.getQuboInstance().getScanTime(CLI.getQuboInstance().getStartTime()));
                    }
                    Log.logln("Bye");
                    System.exit(0);
                    continue block16;
                }
                case "": {
                    continue block16;
                }
            }
            Log.logln("Command \"" + s + "\" not found, digit help to get all commands");
        }
    }
}

