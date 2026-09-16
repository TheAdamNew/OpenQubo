/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo;

import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;
import me.replydev.qubo.Info;
import me.replydev.utils.FileUtils;
import me.replydev.utils.InvalidRangeException;
import me.replydev.utils.IpList;
import me.replydev.utils.PortList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class InputData {
    private IpList ipList;
    private PortList portrange;
    private CommandLine cmd;
    private boolean ping;
    private final String filename;
    private final Options options = this.buildOptions();

    private Options buildOptions() {
        Option iprange = new Option("range", "iprange", true, "The IP range that me.replydev.qubo will scan");
        iprange.setRequired(true);
        Option portrange = new Option("ports", "portrange", true, "The range of ports that me.replydev.qubo will work on");
        portrange.setRequired(true);
        Option threads = new Option("th", "threads", true, "Maximum number of running async threads");
        threads.setRequired(true);
        Option timeout = new Option("ti", "timeout", true, "Server Ping timeout");
        timeout.setRequired(true);
        Option count = new Option("c", "pingcount", true, "How many times me.replydev.qubo will ping a server");
        count.setRequired(false);
        Option noping = new Option("noping", false, "Prevent Qubo from pinging IPs before start scan");
        noping.setRequired(false);
        Option nooutput = new Option("nooutput", false, "Prevent Qubo from creating output file");
        nooutput.setRequired(false);
        Option all = new Option("all", false, "Force Qubo to scan broadcast IPs and common ports");
        all.setRequired(false);
        Option fulloutput = new Option("fulloutput", false, "Create a more readable but bigger output file");
        fulloutput.setRequired(false);
        Option filterVersion = new Option("ver", "filterversion", true, "Show only hits with given version");
        filterVersion.setRequired(false);
        Option filterMotd = new Option("motd", "filtermotd", true, "Show only hits with given motd");
        filterMotd.setRequired(false);
        Option filterOn = new Option("on", "minonline", true, "Show only hits with at least <arg> players online");
        filterOn.setRequired(false);
        Option debug = new Option("d", "debug", false, "Enables debug mode");
        debug.setRequired(false);
        Options options = new Options();
        options.addOption(iprange);
        options.addOption(portrange);
        options.addOption(threads);
        options.addOption(timeout);
        options.addOption(count);
        options.addOption(noping);
        options.addOption(nooutput);
        options.addOption(all);
        options.addOption(fulloutput);
        options.addOption(filterVersion);
        options.addOption(filterMotd);
        options.addOption(filterOn);
        options.addOption(debug);
        return options;
    }

    public void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("-range <arg> -ports <arg> -th <arg> -ti <arg>", this.options);
        System.exit(-1);
    }

    public InputData(String[] command) throws InvalidRangeException, NumberFormatException {
        DefaultParser parser = new DefaultParser();
        String ipStart = "";
        String ipEnd = "";
        try {
            this.cmd = parser.parse(this.options, command);
            try {
                String[] beginEnd = this.cmd.getOptionValue("range").split("-");
                if (beginEnd.length >= 2) {
                    ipStart = this.cmd.getOptionValue("range").split("-")[0];
                    ipEnd = this.cmd.getOptionValue("range").split("-")[1];
                }
                if (IpList.isNotIp(ipStart) || IpList.isNotIp(ipEnd)) {
                    IPAddressSeqRange range = new IPAddressString(this.cmd.getOptionValue("range")).getSequentialRange();
                    ipStart = range.getLower().toString();
                    ipEnd = range.getUpper().toString();
                }
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                if (Info.gui) {
                    throw new InvalidRangeException();
                }
                this.help();
            }
            try {
                this.ipList = new IpList(ipStart, ipEnd);
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
            try {
                this.portrange = new PortList(this.cmd.getOptionValue("ports"));
            }
            catch (NumberFormatException e) {
                if (Info.gui) {
                    throw new NumberFormatException();
                }
                this.help();
            }
            this.ping = !this.cmd.hasOption("noping");
        }
        catch (ParseException e) {
            this.help();
        }
        if (this.isOutput()) {
            this.filename = FileUtils.getCorrectFileName("outputs/" + ipStart + "-" + ipEnd);
            FileUtils.appendToFile("quboScanner by @zreply - Version 0.3.7 ", this.filename);
        } else {
            this.filename = null;
        }
    }

    public boolean isPing() {
        return this.ping;
    }

    public boolean isOutput() {
        return !this.cmd.hasOption("nooutput");
    }

    public boolean isSkipCommonPorts() {
        return !this.cmd.hasOption("all");
    }

    public boolean isFulloutput() {
        return this.cmd.hasOption("fulloutput");
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }

    public int getCount() {
        return Integer.parseInt(this.cmd.getOptionValue("c", "1"));
    }

    public IpList getIpList() {
        return this.ipList;
    }

    public PortList getPortrange() {
        return this.portrange;
    }

    public int getThreads() throws NumberFormatException {
        return Integer.parseInt(this.cmd.getOptionValue("th"));
    }

    public int getTimeout() {
        return Integer.parseInt(this.cmd.getOptionValue("ti"));
    }

    public String getFilename() {
        return this.filename;
    }

    public String getMotd() {
        return this.cmd.getOptionValue("filtermotd", "");
    }

    public String getVersion() {
        return this.cmd.getOptionValue("filterversion", "");
    }

    public int getMinPlayer() {
        return Integer.parseInt(this.cmd.getOptionValue("on", "-1"));
    }

    public boolean isDebugMode() {
        return this.cmd.hasOption("debug");
    }
}

