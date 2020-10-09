package com.example.telegramBotTest2.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOG = Logger.getLogger(Bot.class);
    private static final short PAUSE = 10000;
    public final Queue<Object> sendQueue = new ConcurrentLinkedDeque<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedDeque<>();

    @Override
    public void onUpdateReceived(Update update) {
        LOG.debug("Receive new Update. updateID: " + update.getUpdateId());
        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        return BotConfig.USERNAME_BOT;
    }

    @Override
    public String getBotToken() {
        return BotConfig.TOKEN_BOT;
    }

    public void botConnection() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            LOG.info("Bot started!");
        } catch (TelegramApiRequestException e) {
            LOG.error("Не может соедениться. Ожидание "+PAUSE / 1000+" сек. и выход\n" +
                    "Ошибка: "+e.getMessage());
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException interruptedException) {
                LOG.error(interruptedException.getMessage());
                return;
            }
            botConnection();
        }
    }
}
