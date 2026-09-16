/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.rawData;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.rawData.Extra;

public class ExtraDescription {
    @SerializedName(value="extra")
    private Extra[] extra;

    public String getText() {
        StringBuilder s = new StringBuilder();
        for (Extra e : this.extra) {
            s.append(e.getText());
        }
        return s.toString();
    }
}

