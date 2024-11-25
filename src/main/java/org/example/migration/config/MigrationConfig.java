package org.example.migration.config;

import org.example.migration.utils.PropertiesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.example.migration.utils.PropertiesUtils.CONFIG_FILE;

public class MigrationConfig {
    private final Properties properties;

    public MigrationConfig(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public static MigrationConfig fromFile(String filePath) {
        Properties properties = new Properties();

        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл конфигурации не найден: " + CONFIG_FILE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации: " + CONFIG_FILE, e);
        }

        return new MigrationConfig(properties);
    }

    public static MigrationConfig fromStream(InputStream stream) {
        Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации из потока", e);
        }
        return new MigrationConfig(props);
    }
}
