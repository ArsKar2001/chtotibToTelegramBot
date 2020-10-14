package com.example.telegramBotTest.database;

import com.example.telegramBotTest.bot.Bot;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.*;

public class DBConnection extends DBConfig {

    private static final Logger LOG = Logger.getLogger(DBConnection.class);
    private static final short PAUSE_CONNECT_MS = 10000;
    private static final short EXIT_CONNECT_MS = 30000;
    @Getter
    @Setter
    private Bot bot;
    @Getter
    @Setter
    private Long chatId;

    public DBConnection(Bot bot, Long chatId) {

        this.bot = bot;
        this.chatId = chatId;
    }

    public Connection getConnection() {
        Connection connection;
        String url = "jdbc:mysql://"+getDbHost()+":"+getDbPort()+"/"+getDbName()+"?useSSL=false";
        LOG.info("Connection to DB: "+url);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, getDbLogin(), getDbPass());
            LOG.info("Connection established!");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error(e.getMessage(), e);
            bot.sendQueue.add(
                    new SendMessage().setChatId(chatId).setText("Не могу соединиться с сервером :(\n" +
                            "Через "+PAUSE_CONNECT_MS / 1000+" сек. повтор...")
            );
            try {
                Thread.sleep(PAUSE_CONNECT_MS);
            } catch (InterruptedException interruptedException) {
                LOG.error(interruptedException.getMessage(), interruptedException);
                return null;
            }
            return getConnection();
        }
    }

    public boolean isExists(Long chatId) {
        try (Connection connection = getConnection()) {
            CallableStatement statement = connection.prepareCall("{call isCheckChatId(?)}");
            statement.setLong("chatId", chatId);
            return statement.execute();
        } catch (SQLException throwables) {
            LOG.error(throwables.getMessage(), throwables);
        }
        return false;
    }
}
