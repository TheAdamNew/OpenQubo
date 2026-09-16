# OpenQubo

A Minecraft server scanner that scans IP ranges to find online Minecraft servers. Originally distributed as `qubo.jar`, now open-sourced as a buildable Gradle project.

> **Note:** This build has the automatic version checker removed. The original `qubo.jar` would contact GitHub on startup to check for updates and prompt to download new versions. That functionality has been stripped out.

## Features

- **IP Range Scanning** - Scan entire IP ranges for Minecraft servers
- **Port Range Support** - Scan multiple ports per IP simultaneously
- **Minecraft Protocol Ping** - Retrieves server info (version, MOTD, player count)
- **Forge/FML Support** - Detects modded servers and counts installed mods
- **Filtering** - Filter results by version, MOTD, or minimum player count
- **Dual Mode** - GUI (Swing + FlatLaf Darcula theme) or CLI interface
- **Batch Mode** - Run multiple scans from a `ranges.txt` file
- **ICMP Ping** - Pre-checks host availability before scanning (TCP fallback on non-Windows)

## Building

```bash
./gradlew fatJar
```

Output: `build/libs/qubo-0.3.7-all.jar`

## Usage

### GUI Mode

```bash
java -Dfile.encoding=UTF-8 -jar qubo-0.3.7-all.jar
```

### CLI Mode

```bash
java -Dfile.encoding=UTF-8 -jar qubo-0.3.7-all.jar -range 192.168.1.1-192.168.1.255 -ports 25565-25577 -th 50 -ti 1000
```

### CLI Options

| Option | Description |
|--------|-------------|
| `-range` | IP range to scan (e.g. `192.168.1.1-192.168.1.255` or CIDR `192.168.1.0/24`) |
| `-ports` | Port range to scan (e.g. `25565-25577`) |
| `-th` | Number of threads |
| `-ti` | Ping timeout in milliseconds |
| `-noping` | Skip ICMP pre-check |
| `-all` | Include broadcast IPs and common ports |
| `-fulloutput` | Detailed output format |
| `-motd` | Filter by MOTD |
| `-ver` | Filter by version |
| `-on` | Minimum online players |
| `-nooutput` | Don't write to file |

### Batch Mode

Create a `ranges.txt` file with one scan configuration per line:

```
-range 192.168.1.1-192.168.1.255 -ports 25565-25577 -th 50 -ti 1000
-range 10.0.0.1-10.0.0.255 -ports 25565 -th 100 -ti 500
```

Then run:

```bash
java -Dfile.encoding=UTF-8 -jar qubo-0.3.7-all.jar -txt
```

## Project Structure

```
src/main/java/me/replydev/
├── qubo/           # Core application (Main, CLI, InputData, QuboInstance)
│   └── gui/        # Swing GUI (MainWindow, progress bar, etc.)
├── mcping/         # Minecraft ping protocol implementation
│   ├── data/       # Server response models (Gson)
│   ├── rawData/    # Raw JSON data models
│   └── net/        # TCP/ICMP ping, Windows native ping (JNA)
└── utils/          # Utilities (IP list, port list, file I/O)
```

## Dependencies

- [Gson](https://github.com/google/gson) - JSON parsing
- [Apache HttpClient](https://hc.apache.org/) - HTTP requests
- [FlatLaf](https://www.formdev.com/flatlaf/) - Modern Swing look and feel
- [JNA](https://github.com/java-native-access/jna) - Windows ICMP via native calls
- [ipaddr-java](https://github.com/seancfoley/IPAddress) - IP address/CIDR parsing
- [IntelliJ Forms RT](https://github.com/JetBrains/intellij-community) - GUI form runtime

## License

This project is a decompilation of the original `qubo.jar` by @replydev. Use responsibly and respect the original author's work.
