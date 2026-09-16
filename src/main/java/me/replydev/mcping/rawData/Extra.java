/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;

class Extra {
    @SerializedName(value="color")
    private String color;
    @SerializedName(value="bold")
    private boolean bold;
    @SerializedName(value="text")
    private String text;

    Extra() {
    }

    public String getText() {
        return this.text;
    }
}

