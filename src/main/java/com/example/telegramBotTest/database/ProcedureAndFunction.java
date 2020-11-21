package com.example.telegramBotTest.database;

import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.SQLException;


public class ProcedureAndFunction extends DB {
    private static final Logger LOG = Logger.getLogger(ProcedureAndFunction.class);

    /**
     * Проверка на существование такого ChatId в базе.
     * @param chatId идетификатор чата
     * @return
     */
    public static boolean isExists(Long chatId) {
        CallableStatement statement;
        try {
            statement = getConnection().prepareCall("{call isCheckChatId(?)}");
            statement.setLong("chatId", chatId);
            return statement.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }
}
