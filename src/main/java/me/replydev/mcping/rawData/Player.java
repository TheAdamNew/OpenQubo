/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;

class Player {
    @SerializedName(value="name")
    private String name;
    @SerializedName(value="id")
    private String id;

    Player() {
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
}

