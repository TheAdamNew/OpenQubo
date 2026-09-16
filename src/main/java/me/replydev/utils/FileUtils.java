/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import me.replydev.qubo.Info;

public class FileUtils {
    public static void appendToFile(String s, String filename) {
        if (Info.gui && !filename.contains("json")) {
            return;
        }
        try {
            FileUtils.doAppend(s, filename);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doAppend(String s, String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            f.createNewFile();
        }
        Files.write(Paths.get(filename, new String[0]), (s + "\n").getBytes(), StandardOpenOption.APPEND);
    }

    public static String getCorrectFileName(String filename) {
        File f = new File(filename + ".txt");
        int n = 1;
        while (f.exists()) {
            f = new File(filename + "(" + ++n + ").txt");
        }
        if (n == 1) {
            return filename + ".txt";
        }
        return filename + "(" + n + ").txt";
    }

    public static void createFolder(String folderName) {
        if (Info.gui) {
            return;
        }
        File f = new File(folderName);
        if (f.isDirectory()) {
            return;
        }
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
    }

    public static String getJarName() {
        return new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
    }
}

