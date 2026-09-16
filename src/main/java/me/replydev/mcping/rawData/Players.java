/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import me.replydev.mcping.rawData.Player;

public class Players {
    @SerializedName(value="max")
    private int max;
    @SerializedName(value="online")
    private int online;
    @SerializedName(value="sample")
    private List<Player> sample;

    public int getMax() {
        return this.max;
    }

    public int getOnline() {
        return this.online;
    }

    public List<Player> getSample() {
        return this.sample;
    }
}

