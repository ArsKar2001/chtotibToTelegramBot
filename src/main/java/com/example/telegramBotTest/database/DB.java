package com.example.telegramBotTest.database;

import com.example.telegramBotTest.Config;
import com.example.telegramBotTest.bot.Bot;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.*;

public class DB {
    private static final Logger LOG = Logger.getLogger(DB.class);
    private static final short PAUSE_MS = 10000;

    @Getter
    private static Connection connection;
    public static boolean isConnected = false;

    public void connectingDB() {
        JSONObject dbConfig = Config.getDBConfig();

        String dbHost = (String) dbConfig.get("host");
        String dbName = (String) dbConfig.get("name");
        String dbPort = (String) dbConfig.get("port");
        String dbPass = (String) dbConfig.get("password");
        String dbLogin = (String) dbConfig.get("login");

        String url = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName+"?useSSL=false";
        LOG.info("[STARTED] DB Thread. DB url: "+url);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, dbLogin, dbPass);
            isConnected = true;
            LOG.debug("The connection is established...");
        } catch (ClassNotFoundException | SQLException e) {
            LOG.warn(e.getMessage()+" Repeat after "+PAUSE_MS / 1000 +" sec.");
            isConnected = false;
            try {
                Thread.sleep(PAUSE_MS);
            } catch (InterruptedException interruptedException) {
                LOG.error(interruptedException.getMessage(), interruptedException);
            }
            connectingDB();
        }
    }
}
