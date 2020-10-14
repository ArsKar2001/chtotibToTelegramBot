package com.example.telegramBotTest.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOG = Logger.getLogger(Bot.class);
    private static final short PAUSE_MS = 10000;

    public final ConcurrentHashMap<Long, String> chatIdTextMap = new ConcurrentHashMap<>();

    public final Queue<Object> sendQueue = new ConcurrentLinkedDeque<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedDeque<>();

    @Setter
    @Getter
    private String botName;

    @Setter
    private String botToken;

    @Getter
    private Update update;


    public Bot() {
        this.botName = BotConfig.USERNAME_BOT;
        this.botToken = BotConfig.TOKEN_BOT;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.debug("Receive new Update. updateID: " + update.getUpdateId());
        receiveQueue.add(update);
        this.update = update;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void botConnection() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            LOG.info("[STARTED] TelegramAPI. Bot Connected.");
        } catch (TelegramApiRequestException e) {
            LOG.error("Не может соедениться. Ожидание "+ PAUSE_MS / 1000+" сек. и выход\n" +
                    "Ошибка: "+e.getMessage(), e);
            try {
                Thread.sleep(PAUSE_MS);
            } catch (InterruptedException interruptedException) {
                LOG.error(interruptedException.getMessage(), interruptedException);
                return;
            }
            botConnection();
        }
    }

    public String getMessage(Update update) {
        if(update.hasCallbackQuery()) return update.getCallbackQuery().getData();
        return update.getMessage().getText();
    }

    public Long getChatId(Update update) {
        if(update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getChatId();
        return update.getMessage().getChatId();
    }

    public int getTelegramId(Update update) {
        if(update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getFrom().getId();
        return update.getMessage().getFrom().getId();
    }
}
