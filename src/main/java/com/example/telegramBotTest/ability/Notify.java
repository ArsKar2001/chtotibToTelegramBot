package com.example.telegramBotTest.ability;

import com.example.telegramBotTest.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

public class Notify implements Runnable {

    private static final Logger LOG = Logger.getLogger(Notify.class);
    private static final int MS_IN_SEC = 1000;

    private final Bot bot;
    private final String chatId;
    private final long l;

    public Notify(Bot bot, String chatId, long l) {
        this.bot = bot;
        this.chatId = chatId;
        this.l = l;
        LOG.debug("Создали поток "+toString());
    }

    @Override
    public void run() {
        LOG.info("Выполняем. "+toString());
        bot.sendQueue.add(getFirstMessage());
        try {
            Thread.sleep(l);
            bot.sendQueue.add(getSecondSticker());
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        bot.sendQueue.add(getSecondMessage());
        LOG.info("Закончили. "+toString());
    }

    private SendMessage getSecondMessage() {
        return new SendMessage(chatId, "Время вышло. Спасибо за использование :D");
    }

    private SendSticker getSecondSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker("CAADBQADiQMAAukKyAPZH7wCI2BwFxYE");
        sendSticker.setChatId(chatId);
        return sendSticker;
    }

    private SendMessage getFirstMessage() {
        return new SendMessage(chatId, "Я отправлю вам уведомление через "+l / MS_IN_SEC+" сек.");
    }
}
