/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.rawData.ForgeModListItem;

public class ForgeModInfo {
    @SerializedName(value="type")
    private String type;
    @SerializedName(value="modList")
    private ForgeModListItem[] modList;

    public int getNMods() {
        return this.modList.length;
    }
}

