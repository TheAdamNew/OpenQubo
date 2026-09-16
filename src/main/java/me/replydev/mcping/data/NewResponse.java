/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.data.FinalResponse;
import me.replydev.mcping.data.MCResponse;
import me.replydev.mcping.rawData.Description;
import me.replydev.mcping.rawData.Players;
import me.replydev.mcping.rawData.Version;

public class NewResponse
extends MCResponse {
    @SerializedName(value="description")
    private final Description description = new Description();

    public void setVersion(String a) {
        this.version.setName(a);
    }

    public NewResponse() {
        this.players = new Players();
        this.version = new Version();
    }

    public Description getDescription() {
        return this.description;
    }

    public FinalResponse toFinalResponse() {
        return new FinalResponse(this.players, this.version, this.favicon, this.description.getText());
    }
}

