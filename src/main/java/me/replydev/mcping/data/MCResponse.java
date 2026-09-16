/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.rawData.Players;
import me.replydev.mcping.rawData.Version;

class MCResponse {
    @SerializedName(value="players")
    Players players;
    @SerializedName(value="version")
    Version version;
    @SerializedName(value="favicon")
    String favicon;

    MCResponse() {
    }
}

