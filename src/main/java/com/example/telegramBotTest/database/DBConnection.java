package com.example.telegramBotTest.database;

import org.apache.log4j.Logger;

import java.sql.*;

public class DBConnection extends DBConfig {

    private static final Logger LOG = Logger.getLogger(DBConnection.class);
    private static DBConnection dbConnection = null;

    public static DBConnection getInstance() {
        if(dbConnection == null) dbConnection = new DBConnection();
        return dbConnection;
    }

    public Connection getConnection() {
        Connection connection;
        String url = "jdbc:mysql://"+getDbHost()+":"+getDbPort()+"/"+getDbName();
        LOG.info("Connection to DB - "+getDbName());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, getDbLogin(), getDbPass());
            LOG.info("Connection established.");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public boolean isExists(String chatId) {
        try (Connection connection = getConnection()) {
            CallableStatement statement = connection.prepareCall("{call getChatId(?)}");
            statement.setInt("chatId_", Integer.parseInt(chatId));
            return statement.execute();
        } catch (SQLException throwables) {
            LOG.error(throwables.getMessage(), throwables);
        }
        return false;
    }
}
