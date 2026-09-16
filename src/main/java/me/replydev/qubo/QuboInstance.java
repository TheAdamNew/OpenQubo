/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import me.replydev.mcping.net.Check;
import me.replydev.mcping.net.SimplePing;
import me.replydev.qubo.InputData;
import me.replydev.utils.FileUtils;
import me.replydev.utils.Log;

public class QuboInstance {
    public final InputData inputData;
    private String ip;
    private int port;
    private boolean stop;
    public final AtomicInteger currentThreads;
    private final int[] COMMON_PORTS = new int[]{25, 80, 443, 20, 21, 22, 23, 143, 3306, 3389, 53, 67, 68, 110};
    private long serverCount = 0L;
    private ZonedDateTime start;

    public QuboInstance(InputData inputData) {
        this.inputData = inputData;
        this.currentThreads = new AtomicInteger();
        this.stop = false;
        if (this.inputData.isDebugMode()) {
            Log.logln("Debug mode enabled");
        }
        if (this.inputData.getPortrange().size() < 1500) {
            Log.logln("Skipping the initial ping due to the few ports inserted");
            this.inputData.setPing(false);
        }
    }

    public void run() {
        this.start = ZonedDateTime.now();
        if (this.inputData.isOutput()) {
            FileUtils.appendToFile("Scanner started on: " + this.start.format(DateTimeFormatter.RFC_1123_DATE_TIME), this.inputData.getFilename());
        }
        try {
            this.checkServersExecutor();
        }
        catch (InterruptedException e) {
            Log.log_to_file(e.toString(), "log.txt");
        }
        ZonedDateTime end = ZonedDateTime.now();
        if (this.inputData.isOutput()) {
            FileUtils.appendToFile("Scanner ended on: " + end.format(DateTimeFormatter.RFC_1123_DATE_TIME), this.inputData.getFilename());
        }
        Log.logln(this.getScanTime(this.start, end));
    }

    private void checkServersExecutor() throws InterruptedException, NumberFormatException {
        ExecutorService checkService = Executors.newFixedThreadPool(this.inputData.getThreads());
        Log.logln("Checking Servers...");
        while (this.inputData.getIpList().hasNext()) {
            this.ip = this.inputData.getIpList().getNext();
            try {
                SimplePing simplePing;
                InetAddress address = InetAddress.getByName(this.ip);
                if (this.inputData.isPing() && !(simplePing = new SimplePing(address, this.inputData.getTimeout())).isAlive() || this.inputData.isSkipCommonPorts() && this.isLikelyBroadcast(address)) {
                    continue;
                }
            }
            catch (UnknownHostException unknownHostException) {
                // empty catch block
            }
            while (this.inputData.getPortrange().hasNext()) {
                if (this.stop) {
                    checkService.shutdown();
                    checkService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                    return;
                }
                this.port = this.inputData.getPortrange().get();
                if (this.isCommonPort(this.port)) {
                    this.inputData.getPortrange().next();
                    continue;
                }
                if (this.currentThreads.get() >= this.inputData.getThreads()) continue;
                this.currentThreads.incrementAndGet();
                checkService.execute(new Check(this.ip, this.port, this.inputData.getTimeout(), this.inputData.getFilename(), this.inputData.getCount(), this, this.inputData.getVersion(), this.inputData.getMotd(), this.inputData.getMinPlayer()));
                this.inputData.getPortrange().next();
                ++this.serverCount;
            }
            this.inputData.getPortrange().reload();
        }
        checkService.shutdown();
        checkService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public String getCurrent() {
        return "Current ip: " + this.ip + ":" + this.port + " - (" + String.format("%.2f", this.getPercentage()) + "%)";
    }

    public int getThreads() {
        return this.currentThreads.get();
    }

    public void stop() {
        this.stop = true;
    }

    public String getFilename() {
        return this.inputData.getFilename();
    }

    private boolean isCommonPort(int port) {
        if (!this.inputData.isSkipCommonPorts()) {
            return false;
        }
        for (int i : this.COMMON_PORTS) {
            if (i != port) continue;
            return true;
        }
        return false;
    }

    public double getPercentage() {
        double max = this.inputData.getIpList().getCount() * (long)this.inputData.getPortrange().size();
        return (double)(this.serverCount * 100L) / max;
    }

    private boolean isLikelyBroadcast(InetAddress address) {
        byte[] bytes = address.getAddress();
        return bytes[bytes.length - 1] == 0 || bytes[bytes.length - 1] == -1;
    }

    public ZonedDateTime getStartTime() {
        return this.start;
    }

    public String getScanTime(ZonedDateTime start) {
        return this.getScanTime(start, ZonedDateTime.now());
    }

    public String getScanTime(ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime tempDateTime = ZonedDateTime.from(start);
        long years = tempDateTime.until(end, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);
        long months = tempDateTime.until(end, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);
        long days = tempDateTime.until(end, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);
        long hours = tempDateTime.until(end, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);
        long minutes = tempDateTime.until(end, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);
        long seconds = tempDateTime.until(end, ChronoUnit.SECONDS);
        StringBuilder builder = new StringBuilder();
        if (seconds != 0L) {
            builder.append(seconds).append(" seconds");
        }
        if (minutes != 0L) {
            builder.insert(0, minutes + " minutes, ");
        }
        if (hours != 0L) {
            builder.insert(0, minutes + " hours, ");
        }
        if (days != 0L) {
            builder.insert(0, minutes + " days, ");
        }
        if (months != 0L) {
            builder.insert(0, minutes + " months, ");
        }
        if (years != 0L) {
            builder.insert(0, minutes + " years, ");
        }
        builder.insert(0, "Scan time: ");
        return builder.toString();
    }
}

