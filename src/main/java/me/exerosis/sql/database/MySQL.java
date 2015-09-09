package me.exerosis.sql.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {
    public MySQL(String dbName) {
        super(dbName);
    }

    @Override
    public boolean openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + getHostname() + ":" + getPort() + "/" + getDBName(), getUsername(), getPassword());
        } catch (SQLException e) {
            connection = null;
        }
        try {
            if (connection == null || connection.isClosed())
                return false;
        } catch (SQLException ignored) {
        }
        return true;
    }
}