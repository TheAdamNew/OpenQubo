/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.mcping.net;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import me.replydev.mcping.MCPing;
import me.replydev.mcping.PingOptions;
import me.replydev.mcping.data.FinalResponse;
import me.replydev.qubo.Info;
import me.replydev.qubo.QuboInstance;
import me.replydev.qubo.gui.MainWindow;
import me.replydev.utils.FileUtils;

public class Check
implements Runnable {
    private final String hostname;
    private final int port;
    private final String filename;
    private final int timeout;
    private final int count;
    private final QuboInstance quboInstance;
    private final String filterVersion;
    private final String filterMotd;
    private final int minPlayer;

    public Check(String hostname, int port, int timeout, String filename, int count, QuboInstance quboInstance, String filterVersion, String filterMotd, int minPlayer) {
        this.hostname = hostname;
        this.port = port;
        this.filename = filename;
        this.timeout = timeout;
        this.count = count;
        this.quboInstance = quboInstance;
        this.filterVersion = filterVersion;
        this.filterMotd = filterMotd;
        this.minPlayer = minPlayer;
    }

    @Override
    public void run() {
        this.check();
        this.quboInstance.currentThreads.decrementAndGet();
    }

    private void check() {
        if (this.hostname == null || this.filterVersion == null || this.filterMotd == null) {
            return;
        }
        for (int i = 0; i < this.count; ++i) {
            try {
                long time = System.currentTimeMillis();
                try {
                    FinalResponse response = new MCPing().getPing(new PingOptions().setHostname(this.hostname).setPort(this.port).setTimeout(this.timeout));
                    if (response == null) continue;
                    if (response.getDescription().contains(this.filterMotd) && response.getVersion().getName().contains(this.filterVersion) && response.getPlayers().getOnline() > this.minPlayer) {
                        String des = Check.getGoodDescription(response.getDescription());
                        String dati = "-----------------------\n" + this.hostname + ":" + this.port + "\nVersion: " + response.getVersion().getName() + "\nOnline: " + response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + "\nMOTD: " + des + "\nPing time: " + (System.currentTimeMillis() - time) + " ms";
                        String singleLine = "(" + this.hostname + ":" + this.port + ")(" + response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + ")(" + response.getVersion().getName() + ")(" + des + ")";
                        ++Info.serverFound;
                        ++Info.serverNotFilteredFound;
                        if (Info.gui) {
                            MainWindow.dtm.addRow(new Object[]{Info.serverFound, this.hostname, this.port, response.getPlayers().getOnline() + "/" + response.getPlayers().getMax(), response.getVersion().getName(), des});
                        } else {
                            System.out.println(singleLine);
                        }
                        if (this.quboInstance.inputData.isOutput() && Files.exists(Paths.get(this.filename, new String[0]), new LinkOption[0])) {
                            if (this.quboInstance.inputData.isFulloutput()) {
                                FileUtils.appendToFile(dati, this.filename);
                            } else {
                                FileUtils.appendToFile(singleLine, this.filename);
                            }
                        }
                    } else {
                        ++Info.serverNotFilteredFound;
                    }
                    return;
                }
                catch (JsonSyntaxException e) {
                    System.out.println("(" + this.hostname + ":" + this.port + ")(Json not readable)");
                    if (this.quboInstance.inputData.isOutput()) {
                        FileUtils.appendToFile("-----------------------" + this.hostname + ":" + this.port + "\nJson not readable", this.filename);
                    }
                    ++Info.serverNotFilteredFound;
                }
                catch (NullPointerException e) {
                    if (!this.quboInstance.inputData.isDebugMode()) continue;
                    System.out.println("WARN: NullPointerException for: " + this.hostname + ":" + this.port);
                }
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static String getGoodDescription(String des) {
        if (des == null) {
            return "";
        }
        des = des.replace("\u00a70", "");
        des = des.replace("\u00a71", "");
        des = des.replace("\u00a72", "");
        des = des.replace("\u00a73", "");
        des = des.replace("\u00a74", "");
        des = des.replace("\u00a75", "");
        des = des.replace("\u00a76", "");
        des = des.replace("\u00a77", "");
        des = des.replace("\u00a78", "");
        des = des.replace("\u00a79", "");
        des = des.replace("\u00a7a", "");
        des = des.replace("\u00a7b", "");
        des = des.replace("\u00a7c", "");
        des = des.replace("\u00a7d", "");
        des = des.replace("\u00a7e", "");
        des = des.replace("\u00a7f", "");
        des = des.replace("\u00a7l", "");
        des = des.replace("\u00a7m", "");
        des = des.replace("\u00a7n", "");
        des = des.replace("\u00a7o", "");
        des = des.replace("\u00a7r", "");
        des = des.trim().replaceAll(" +", " ");
        des = des.replace("\n", "");
        return des;
    }
}

