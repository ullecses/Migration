package org.example.migration.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ConnectionManager {

    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    // Загружаем параметры подключения только один раз при запуске
    static {
        loadConnectionDetails();
    }

    // Метод для загрузки параметров подключения из конфигурационного файла
    private static void loadConnectionDetails() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("db.properties")) {
            properties.load(inputStream);
            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке параметров подключения из файла", e);
        }
    }

    /**
     * Получить соединение с базой данных.
     * @return Connection объект для работы с базой данных.
     * @throws SQLException если не удается установить соединение.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new SQLException("Не удалось получить соединение с базой данных: " + e.getMessage(), e);
        }
    }

    /**
     * Закрыть соединение.
     * @param connection соединение, которое нужно закрыть.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Исключение при закрытии соединения не должно прерывать основной поток
                System.err.println("Ошибка при закрытии соединения с базой данных: " + e.getMessage());
            }
        }
    }
}