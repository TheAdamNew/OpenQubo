/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;

class TCPPinger {
    private static final int[] PROBE_TCP_PORTS = new int[]{80, 443, 8080, 22, 7};
    private final int timeout;

    public TCPPinger(int timeout) {
        this.timeout = timeout;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean ping(InetAddress address, int count) {
        for (int i = 0; i < count && !Thread.currentThread().isInterrupted(); ++i) {
            Socket socket = new Socket();
            int probePort = PROBE_TCP_PORTS[i % PROBE_TCP_PORTS.length];
            try {
                socket.setReuseAddress(true);
                socket.setReceiveBufferSize(32);
                socket.connect(new InetSocketAddress(address, probePort), this.timeout);
                if (!socket.isConnected()) continue;
                TCPPinger.closeQuietly(socket);
                boolean bl = true;
                return bl;
            }
            catch (SocketTimeoutException socketTimeoutException) {
                continue;
            }
            catch (NoRouteToHostException e) {
                break;
            }
            catch (IOException e) {
                String msg = e.getMessage();
                if (msg.contains("refused")) {
                    TCPPinger.closeQuietly(socket);
                    boolean bl = true;
                    return bl;
                }
                if (!msg.contains("route to host") && !msg.contains("down") && !msg.contains("unreachable") && !msg.contains("closed")) continue;
                break;
            }
            finally {
                TCPPinger.closeQuietly(socket);
            }
        }
        return false;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }
}

