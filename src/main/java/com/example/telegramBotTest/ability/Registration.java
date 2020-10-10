package com.example.telegramBotTest.ability;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.database.DBConnection;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Registration implements Runnable {

    private static final Logger LOG = Logger.getLogger(Registration.class);

    private final Bot bot;
    private final String chatId;
    private final DBConnection dbConnection;

    public Registration(Bot bot, String chatId) {
        this.bot = bot;
        this.chatId = chatId;
        dbConnection = DBConnection.getInstance();
    }

    @Override
    public void run() {
        LOG.info("Начинаем процесс регистрации для "+bot.getUpdate().getMessage().getFrom().getUserName());
        if(!dbConnection.isExists(chatId)) {
            bot.sendQueue.add(getFirstMessage());
            bot.receiveQueue.add(bot.getUpdate());
        } else {
            bot.sendQueue.add(getMessageExists());
        }
        LOG.info("Закончили процесс для "+bot.getUpdate().getMessage().getFrom().getUserName());
    }

    private SendMessage getFirstMessage() {
        return null;
    }

    private SendMessage getMessageExists() {
        return new SendMessage(chatId, "Похоже, что ты уже зарегистрирован :D");
    }
}
