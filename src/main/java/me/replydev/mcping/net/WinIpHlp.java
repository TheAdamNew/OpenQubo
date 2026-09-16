/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import java.net.InetAddress;
import me.replydev.mcping.net.WinIpHlpDll;

class WinIpHlp {
    WinIpHlp() {
    }

    public static WinIpHlpDll.IpAddrByVal toIpAddr(InetAddress address) {
        WinIpHlpDll.IpAddrByVal addr = new WinIpHlpDll.IpAddrByVal();
        addr.bytes = address.getAddress();
        return addr;
    }

    public static WinIpHlpDll.Ip6SockAddrByRef toIp6Addr(InetAddress address) {
        WinIpHlpDll.Ip6SockAddrByRef addr = new WinIpHlpDll.Ip6SockAddrByRef();
        addr.bytes = address.getAddress();
        return addr;
    }
}

