/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import java.io.IOException;
import java.net.InetAddress;
import me.replydev.mcping.net.TCPPinger;
import me.replydev.mcping.net.WindowsPinger;
import me.replydev.utils.SystemSpecs;

public class SimplePing {
    private final InetAddress address;
    private final int timeout;

    public SimplePing(InetAddress address, int timeout) {
        this.address = address;
        this.timeout = timeout;
    }

    public boolean isAlive() {
        SystemSpecs specs = new SystemSpecs();
        if (specs.getOperatingSystem().contains("windows")) {
            WindowsPinger pinger = new WindowsPinger(this.timeout);
            try {
                boolean response = pinger.ping(this.address, 2);
                return response;
            }
            catch (IOException e) {
                return true;
            }
        }
        TCPPinger pinger = new TCPPinger(this.timeout);
        boolean response = pinger.ping(this.address, 2);
        return response;
    }
}

