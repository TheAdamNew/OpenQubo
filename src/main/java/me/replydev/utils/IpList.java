/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.utils;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class IpList {
    private final long start;
    private final long end;
    private long index;
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean isNotIp(String ip) {
        return !PATTERN.matcher(ip).matches();
    }

    public IpList(String _start, String _end) {
        if (IpList.isNotIp(_start)) {
            throw new IllegalArgumentException(_start + " is not a valid ip!");
        }
        if (IpList.isNotIp(_end)) {
            throw new IllegalArgumentException(_end + " is not a valid ip!");
        }
        this.start = IpList.host2long(_start);
        this.end = IpList.host2long(_end);
        this.index = this.start;
    }

    public boolean hasNext() {
        return this.index <= this.end;
    }

    public long getCount() {
        return this.end - this.start;
    }

    public String getNext() {
        String data = IpList.long2dotted(this.index);
        ++this.index;
        return data;
    }

    private static long host2long(String host) {
        long ip = 0L;
        if (!Character.isDigit(host.charAt(0))) {
            return -1L;
        }
        int[] addr = IpList.ip2intarray(host);
        if (addr == null) {
            return -1L;
        }
        for (int i = 0; i < addr.length; ++i) {
            ip += (long)Math.max(addr[i], 0) << 8 * (3 - i);
        }
        return ip;
    }

    private static int[] ip2intarray(String host) {
        int[] address = new int[]{-1, -1, -1, -1};
        int i = 0;
        StringTokenizer tokens = new StringTokenizer(host, ".");
        if (tokens.countTokens() > 4) {
            return null;
        }
        while (tokens.hasMoreTokens()) {
            try {
                address[i++] = Integer.parseInt(tokens.nextToken()) & 0xFF;
            }
            catch (NumberFormatException nfe) {
                return null;
            }
        }
        return address;
    }

    private static String long2dotted(long address) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int shift = 24;
        while (i < 4) {
            long value = address >> shift & 0xFFL;
            sb.append(value);
            if (i != 3) {
                sb.append('.');
            }
            ++i;
            shift -= 8;
        }
        return sb.toString();
    }
}

