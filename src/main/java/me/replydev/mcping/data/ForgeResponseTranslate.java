/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.data.FinalResponse;
import me.replydev.mcping.rawData.ForgeDescriptionTranslate;
import me.replydev.mcping.rawData.ForgeModInfo;
import me.replydev.mcping.rawData.Players;
import me.replydev.mcping.rawData.Version;

public class ForgeResponseTranslate {
    @SerializedName(value="description")
    private ForgeDescriptionTranslate description;
    @SerializedName(value="players")
    private Players players;
    @SerializedName(value="version")
    private Version version;
    @SerializedName(value="modinfo")
    private ForgeModInfo modinfo;

    public FinalResponse toFinalResponse() {
        this.version.setName(this.version.getName() + " FML with " + this.modinfo.getNMods() + " mods");
        return new FinalResponse(this.players, this.version, "", this.description.getTranslate());
    }
}

