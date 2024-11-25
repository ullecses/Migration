package org.example.migration.source.impl;

import org.example.migration.source.MigrationSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileSystemMigrationSource implements MigrationSource {
    private final String directoryPath;

    public FileSystemMigrationSource(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public List<InputStream> getMigrationFiles() {
        List<InputStream> files = new ArrayList<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является директорией: " + directoryPath);
        }

        File[] sqlFiles = directory.listFiles((dir, name) -> name.endsWith(".sql"));
        if (sqlFiles != null) {
            for (File file : sqlFiles) {
                try {
                    files.add(new FileInputStream(file));
                } catch (Exception e) {
                    throw new RuntimeException("Ошибка чтения файла миграции: " + file.getName(), e);
                }
            }
        }
        return files;
    }
}