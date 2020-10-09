package com.example.telegramBotTest2.service;

import com.example.telegramBotTest2.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender implements Runnable {

    private static final Logger LOG = Logger.getLogger(MessageSender.class);
    private final short SLEEP_TIME_MS = 1000;
    private Bot bot;

    public MessageSender(Bot bot) {
        this.bot = bot;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * Проверяет очередь на отправку и вызывает команду send.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOG.info("[STARTED] MessageSender. Bot class - "+bot);
        try {
            while (true) {
                for (Object o : bot.sendQueue) {
                    LOG.debug("Получение нового сообщение для отправки "+o);
                    send(o);
                }
                try {
                    Thread.sleep(SLEEP_TIME_MS);
                } catch (InterruptedException  e) {
                    LOG.error("Прерывание при работе со списком сообщений", e);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * Определяет тип сообщения для отправки и применяет к нему соответствую команду
     * @param o
     */
    private void send(Object o) {
        try {
            MessageType type = messageType(o);
            switch (type) {
                case EXECUTE:
                    BotApiMethod<Message> messageBotApiMethod = (BotApiMethod<Message>) o;
                    LOG.debug("Использован EXECUTE для "+messageBotApiMethod);
                    bot.execute(messageBotApiMethod);
                    break;
                case STICKER:
                    SendSticker sendSticker = (SendSticker) o;
                    LOG.debug("Использован SendSticker для "+sendSticker);
                    bot.execute(sendSticker);
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private MessageType messageType(Object o) {
        if(o instanceof SendSticker) return MessageType.STICKER;
        if(o instanceof BotApiMethod) return MessageType.EXECUTE;
        return MessageType.NOT_DETECTED;
    }

    /**
     *
     */
    enum MessageType {
        EXECUTE, STICKER, NOT_DETECTED
    }
}
