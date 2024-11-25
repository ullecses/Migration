package org.example.migration;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MigrationTool()).execute(args);
        System.exit(exitCode);
    }
}
