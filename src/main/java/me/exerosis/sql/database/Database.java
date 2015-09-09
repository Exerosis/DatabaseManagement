package me.exerosis.sql.database;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    protected Connection connection = null;
    protected Statement statement;
    protected String _password;
    private String _username;
    private String _hostname;
    private int _port;
    private String _dbName;

    protected Database(String dbName) {
        retriveDBInfo(dbName);
        openConnection();
    }

    private void retriveDBInfo(String dbName) {
        File dbFolder = new File("SQL/DBs");
        if (!dbFolder.isDirectory()) {
            dbFolder.mkdirs();
            Log.warn("No such directory SQL/DBs, created one!");
        }

        File dbFile = new File("SQL/DBs/" + dbName + ".yml");
        if (!dbFile.isFile()) {
            Log.warn("No such file '" + dbName + "' created one!");
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = new YamlConfiguration().loadConfiguration(dbFile);
        _username = config.getString("username");
        _password = config.getString("password");
        _dbName = dbName;
        _port = config.getInt("port");
        _hostname = config.getString("hostname");
    }

    public abstract boolean openConnection();

    public boolean isOnline() {
        return openConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        try {
            if (statement == null || statement.isClosed())
                statement = connection.createStatement();
        } catch (SQLException e) {
        }
        return statement;
    }

    protected boolean closeConnection() throws SQLException {
        if (connection == null)
            return false;
        connection.close();
        return true;
    }

    protected ResultSet querySQL(String query) throws SQLException {
        if (statement == null || statement.isClosed())
            statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    protected int updateSQL(String query) throws SQLException {
        if (statement == null || statement.isClosed())
            statement = connection.createStatement();
        return statement.executeUpdate(query);
    }


    public String getDBName() {
        return _dbName;
    }

    public String getHostname() {
        return _hostname;
    }

    public String getPassword() {
        return _password;
    }

    public int getPort() {
        return _port;
    }

    public String getUsername() {
        return _username;
    }
}