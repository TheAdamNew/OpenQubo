/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetSocketAddress;
import me.replydev.mcping.PingOptions;
import me.replydev.mcping.Pinger;
import me.replydev.mcping.data.ExtraResponse;
import me.replydev.mcping.data.FinalResponse;
import me.replydev.mcping.data.ForgeResponse;
import me.replydev.mcping.data.ForgeResponseOld;
import me.replydev.mcping.data.ForgeResponseTranslate;
import me.replydev.mcping.data.NewResponse;
import me.replydev.mcping.data.OldResponse;

public class MCPing {
    public FinalResponse getPing(PingOptions options) throws IOException {
        Gson gson = new Gson();
        Pinger a = new Pinger();
        a.setAddress(new InetSocketAddress(options.getHostname(), options.getPort()));
        a.setTimeout(options.getTimeout());
        String json = a.fetchData();
        if (json != null && json.contains("{")) {
            if (json.contains("\"modid\"") && json.contains("\"translate\"")) {
                return gson.fromJson(json, ForgeResponseTranslate.class).toFinalResponse();
            }
            if (json.contains("\"modid\"") && json.contains("\"text\"")) {
                return gson.fromJson(json, ForgeResponse.class).toFinalResponse();
            }
            if (json.contains("\"modid\"")) {
                return gson.fromJson(json, ForgeResponseOld.class).toFinalResponse();
            }
            if (json.contains("\"extra\"")) {
                return gson.fromJson(json, ExtraResponse.class).toFinalResponse();
            }
            if (json.contains("\"text\"")) {
                return gson.fromJson(json, NewResponse.class).toFinalResponse();
            }
            return gson.fromJson(json, OldResponse.class).toFinalResponse();
        }
        return null;
    }
}

