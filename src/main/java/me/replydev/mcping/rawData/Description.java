/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;

public class Description {
    @SerializedName(value="text")
    private String text;

    public String getText() {
        return this.text;
    }
}

