/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.data;

import me.replydev.mcping.data.MCResponse;
import me.replydev.mcping.rawData.Players;
import me.replydev.mcping.rawData.Version;

public class FinalResponse
extends MCResponse {
    private final String description;

    public FinalResponse(Players players, Version version, String favicon, String description) {
        this.description = description;
        this.favicon = favicon;
        this.players = players;
        this.version = version;
    }

    public Players getPlayers() {
        return this.players;
    }

    public Version getVersion() {
        return this.version;
    }

    public String getDescription() {
        return this.description;
    }
}

