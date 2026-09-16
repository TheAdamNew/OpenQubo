/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.data.FinalResponse;
import me.replydev.mcping.data.MCResponse;
import me.replydev.mcping.rawData.ExtraDescription;

public class ExtraResponse
extends MCResponse {
    @SerializedName(value="description")
    private ExtraDescription description;

    public FinalResponse toFinalResponse() {
        return new FinalResponse(this.players, this.version, this.favicon, this.description.getText());
    }
}

