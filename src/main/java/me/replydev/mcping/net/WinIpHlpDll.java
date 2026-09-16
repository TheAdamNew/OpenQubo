/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public interface WinIpHlpDll
extends Library {
    public static final WinIpHlpDll dll = Loader.load();

    public Pointer IcmpCreateFile();

    public Pointer Icmp6CreateFile();

    public void IcmpCloseHandle(Pointer var1);

    public int IcmpSendEcho(Pointer var1, IpAddrByVal var2, Pointer var3, short var4, IpOptionInformationByRef var5, Pointer var6, int var7, int var8);

    public int Icmp6SendEcho2(Pointer var1, Pointer var2, Pointer var3, Pointer var4, Ip6SockAddrByRef var5, Ip6SockAddrByRef var6, Pointer var7, short var8, IpOptionInformationByRef var9, Pointer var10, int var11, int var12);

    public int SendARP(IpAddrByVal var1, int var2, Pointer var3, Pointer var4);

    public static class Loader {
        static WinIpHlpDll load() {
            try {
                return Native.load("iphlpapi", WinIpHlpDll.class);
            }
            catch (UnsatisfiedLinkError e) {
                return Native.load("icmp", WinIpHlpDll.class);
            }
        }
    }

    public static class Icmp6EchoReply
    extends AutoOrderedStructure {
        public short port;
        public byte[] flowInfo = new byte[4];
        public final byte[] addressBytes = new byte[16];
        public int scopeId;
        public int status;
        public int roundTripTime;

        public Icmp6EchoReply() {
        }

        public Icmp6EchoReply(Pointer p) {
            this.useMemory(p);
            this.read();
        }
    }

    public static class IcmpEchoReply
    extends AutoOrderedStructure {
        public IpAddrByVal address;
        public int status;
        public int roundTripTime;
        public short dataSize;
        public short reserved;
        public Pointer data;
        public IpOptionInformationByVal options;

        public IcmpEchoReply() {
        }

        public IcmpEchoReply(Pointer p) {
            this.useMemory(p);
            this.read();
        }
    }

    public static class IpOptionInformationByRef
    extends IpOptionInformation
    implements Structure.ByReference {
    }

    public static class IpOptionInformationByVal
    extends IpOptionInformation
    implements Structure.ByValue {
    }

    public static class IpOptionInformation
    extends AutoOrderedStructure {
        public byte ttl;
        public byte tos;
        public byte flags;
        public byte optionsSize;
        public Pointer optionsData;
    }

    public static class Ip6SockAddrByRef
    extends Ip6SockAddr
    implements Structure.ByReference {
    }

    public static class Ip6SockAddr
    extends AutoOrderedStructure {
        public short family = (short)10;
        public short port;
        public int flowInfo;
        public byte[] bytes = new byte[16];
        public int scopeId;
    }

    public static class IpAddrByVal
    extends IpAddr
    implements Structure.ByValue {
    }

    public static class IpAddr
    extends AutoOrderedStructure {
        public byte[] bytes = new byte[4];
    }

    public static class AutoOrderedStructure
    extends Structure {
        @Override
        protected List<String> getFieldOrder() {
            ArrayList<String> fields = new ArrayList<String>();
            for (Field field : this.getClass().getFields()) {
                if (Modifier.isStatic(field.getModifiers())) continue;
                fields.add(field.getName());
            }
            return fields;
        }
    }
}

