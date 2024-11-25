package org.example.migration.source.impl;

import org.example.migration.source.MigrationSource;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class ClasspathMigrationSource implements MigrationSource {
    private final String resourcePath;

    public ClasspathMigrationSource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public List<InputStream> getMigrationFiles() {
        List<InputStream> files = new ArrayList<>();
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources(resourcePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    // Для локальных файлов (при запуске из IDE или файловой системы)
                    var directory = new java.io.File(resource.toURI());
                    if (directory.isDirectory()) {
                        for (var file : Objects.requireNonNull(directory.listFiles((dir, name) -> name.endsWith(".sql")))) {
                            files.add(file.toURI().toURL().openStream());
                        }
                    }
                } else if (resource.getProtocol().equals("jar")) {
                    // Для упакованных JAR-файлов
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    try (var jarFile = new java.util.jar.JarFile(jarPath)) {
                        var entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            var entry = entries.nextElement();
                            if (entry.getName().startsWith(resourcePath) && entry.getName().endsWith(".sql")) {
                                files.add(getClass().getClassLoader().getResourceAsStream(entry.getName()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке миграционных файлов из ресурса: " + resourcePath, e);
        }
        return files;
    }
}