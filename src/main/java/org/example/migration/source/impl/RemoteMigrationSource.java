package org.example.migration.source.impl;

import org.example.migration.source.MigrationSource;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteMigrationSource implements MigrationSource {
    private final List<String> urls;

    public RemoteMigrationSource(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public List<InputStream> getMigrationFiles() {
        List<InputStream> files = new ArrayList<>();
        for (String urlString : urls) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                files.add(connection.getInputStream());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка загрузки файла миграции по URL: " + urlString, e);
            }
        }
        return files;
    }
}
