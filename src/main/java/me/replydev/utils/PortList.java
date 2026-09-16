/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.utils;

import java.util.Iterator;

public final class PortList
implements Iterator<Integer>,
Cloneable {
    private int[] portRangeStart;
    private int[] portRangeEnd;
    private int rangeCountMinus1;
    private int rangeIndex;
    private int currentPort;
    private boolean hasNext;
    private String[] portRanges;

    public PortList(String portString) throws NumberFormatException {
        if (portString != null && (portString = portString.trim()).length() > 0) {
            this.portRanges = portString.split("[\\s\t\n\r,.;]+");
            this.portRangeStart = new int[this.portRanges.length + 1];
            this.portRangeEnd = new int[this.portRanges.length];
            for (int i = 0; i < this.portRanges.length; ++i) {
                int endPort;
                String range = this.portRanges[i];
                int dashPos = range.indexOf(45) + 1;
                this.portRangeEnd[i] = endPort = Integer.parseInt(range.substring(dashPos));
                int n = this.portRangeStart[i] = dashPos == 0 ? endPort : Integer.parseInt(range.substring(0, dashPos - 1));
                if (endPort > 0 && endPort < 65536) continue;
                throw new NumberFormatException(endPort + " port is out of range");
            }
            this.reload();
        }
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public Integer next() {
        int returnPort = this.currentPort++;
        if (this.currentPort > this.portRangeEnd[this.rangeIndex]) {
            this.hasNext = this.rangeIndex < this.rangeCountMinus1;
            ++this.rangeIndex;
            this.currentPort = this.portRangeStart[this.rangeIndex];
        }
        return returnPort;
    }

    public Integer get() {
        return this.currentPort;
    }

    public int size() {
        int size = 0;
        if (this.portRangeStart != null) {
            for (int i = 0; i <= this.rangeCountMinus1; ++i) {
                size += this.portRangeEnd[i] - this.portRangeStart[i] + 1;
            }
        }
        return size;
    }

    public PortList copy() {
        try {
            return (PortList)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void reload() {
        this.currentPort = this.portRangeStart[0];
        this.rangeCountMinus1 = this.portRanges.length - 1;
        this.rangeIndex = 0;
        this.hasNext = this.rangeCountMinus1 >= 0;
    }
}

