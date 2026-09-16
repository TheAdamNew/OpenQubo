/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;

public class Version {
    @SerializedName(value="name")
    private String name;
    @SerializedName(value="protocol")
    private int protocol;

    public void setName(String a) {
        this.name = a;
    }

    public String getName() {
        return this.name;
    }
}

