package org.example.appservlet.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManagerImpl implements ConnectionManager {
    private static final String PROPERTIES_FILE = "database.properties";
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    public ConnectionManagerImpl() {
        loadDriver();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties props = getProperties();

        return DriverManager.getConnection(props.getProperty("jdbc.url"),
                props.getProperty("jdbc.username"),
                props.getProperty("jdbc.password"));
    }

    private void loadDriver() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удается загрузить класс драйвера!");
        }
    }

    private Properties getProperties() throws RuntimeException {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Не удается загрузить свойства базы данных!", e);
        }

        return props;
    }
}
