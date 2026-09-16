/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Arrays;
import me.replydev.mcping.net.TCPPinger;
import me.replydev.mcping.net.WinIpHlp;
import me.replydev.mcping.net.WinIpHlpDll;

class WindowsPinger {
    private final int timeout;
    private final WinIpHlpDll.Ip6SockAddrByRef anyIp6SourceAddr = new WinIpHlpDll.Ip6SockAddrByRef();

    public WindowsPinger(int timeout) {
        this.timeout = timeout;
    }

    public boolean ping(InetAddress address, int count) throws IOException {
        if (address instanceof Inet6Address) {
            return this.ping6(address, count);
        }
        return this.ping4(address, count);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean ping4(InetAddress address, int count) throws IOException {
        Pointer handle = WinIpHlpDll.dll.IcmpCreateFile();
        if (handle == null) {
            throw new IOException("Unable to create Windows native ICMP handle");
        }
        int sendDataSize = 32;
        int replyDataSize = sendDataSize + new WinIpHlpDll.IcmpEchoReply().size() + 10;
        Memory sendData = new Memory(sendDataSize);
        sendData.clear(sendDataSize);
        Memory replyData = new Memory(replyDataSize);
        try {
            WinIpHlpDll.IpAddrByVal ipaddr = WinIpHlp.toIpAddr(address);
            for (int i = 1; i <= count && !Thread.currentThread().isInterrupted(); ++i) {
                int numReplies = WinIpHlpDll.dll.IcmpSendEcho(handle, ipaddr, sendData, (short)sendDataSize, null, replyData, replyDataSize, this.timeout);
                WinIpHlpDll.IcmpEchoReply echoReply = new WinIpHlpDll.IcmpEchoReply(replyData);
                if (numReplies <= 0 || echoReply.status != 0 || !Arrays.equals(echoReply.address.bytes, ipaddr.bytes)) continue;
                boolean bl = true;
                return bl;
            }
        }
        finally {
            WinIpHlpDll.dll.IcmpCloseHandle(handle);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean ping6(InetAddress address, int count) throws IOException {
        Pointer handle = WinIpHlpDll.dll.Icmp6CreateFile();
        if (handle == null) {
            throw new IOException("Unable to create Windows native ICMP6 handle");
        }
        int sendDataSize = 32;
        int replyDataSize = sendDataSize + new WinIpHlpDll.Icmp6EchoReply().size() + 10;
        Memory sendData = new Memory(sendDataSize);
        sendData.clear(sendDataSize);
        Memory replyData = new Memory(replyDataSize);
        try {
            WinIpHlpDll.Ip6SockAddrByRef ipaddr = WinIpHlp.toIp6Addr(address);
            for (int i = 1; i <= count && !Thread.currentThread().isInterrupted(); ++i) {
                int numReplies = WinIpHlpDll.dll.Icmp6SendEcho2(handle, null, null, null, this.anyIp6SourceAddr, WinIpHlp.toIp6Addr(address), sendData, (short)sendDataSize, null, replyData, replyDataSize, this.timeout);
                WinIpHlpDll.Icmp6EchoReply echoReply = new WinIpHlpDll.Icmp6EchoReply(replyData);
                if (numReplies <= 0 || echoReply.status != 0 || !Arrays.equals(echoReply.addressBytes, ipaddr.bytes)) continue;
                WinIpHlpDll.dll.IcmpCloseHandle(handle);
                boolean bl = true;
                return bl;
            }
        }
        finally {
            WinIpHlpDll.dll.IcmpCloseHandle(handle);
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        long now = System.currentTimeMillis();
        System.out.println(new TCPPinger(5000).ping(InetAddress.getByName("164.132.200.78"), 1));
        System.out.println(System.currentTimeMillis() - now);
        now = System.currentTimeMillis();
        System.out.println(new WindowsPinger(5000).ping(InetAddress.getByName("164.132.200.78"), 1));
        System.out.println(System.currentTimeMillis() - now);
    }
}

