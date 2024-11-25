package org.example.migration.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MigrationFileReader {
    private static final Pattern MIGRATION_FILE_PATTERN = Pattern.compile("^V\\d+__.+\\.sql$");

    public static List<File> getMigrationFiles(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является директорией: " + directoryPath);
        }

        List<File> migrationFiles = new ArrayList<>();
        File[] files = directory.listFiles((dir, name) -> MIGRATION_FILE_PATTERN.matcher(name).matches());

        if (files != null) {
            for (File file : files) {
                migrationFiles.add(file);
            }
        }

        migrationFiles.sort((f1, f2) -> f1.getName().compareTo(f2.getName())); // Сортировка по имени
        return migrationFiles;
    }
}
