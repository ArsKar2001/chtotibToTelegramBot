package com.example.telegramBotTest.service;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.commands.Parser;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.handler.AbstractHandler;
import com.example.telegramBotTest.handler.SystemHandler;
import com.example.telegramBotTest.handler.NotifyHandler;
import com.example.telegramBotTest.handler.DefaultHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Thread.sleep;


public class MessageReceiver implements Runnable {

    private static final Logger LOG = Logger.getLogger(MessageReceiver.class);
    private static final short WAIT_NEW_MESSAGE = 1000;
    private final Bot bot;
    private final Parser parser;

    public MessageReceiver(Bot bot) {
        this.bot = bot;
        parser = new Parser(bot.getBotName());
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
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOG.info("[STARTED] MessageReceiver. Bot class - " + bot);
        try {
            while (true) {
                for (Object o = bot.receiveQueue.poll(); o != null; o = bot.receiveQueue.poll()) {
                    LOG.debug("Новый объект для анализа "+o.toString());
                    analyze(o);
                }
                try {
                    sleep(WAIT_NEW_MESSAGE);
                } catch (InterruptedException  e) {
                    LOG.error("Вышло прерывание. Выход ", e);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Проверяет тип объекта
     * @param o
     */
    private void analyze(Object o) {
        if(o instanceof Update) {
            Update update = (Update) o;
            LOG.debug("Update received: "+update.toString());
            analyzeForUpdateType(update);
        } else {
            LOG.warn("Unknown type object: "+o.toString());
        }
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        ParserCommand parserCommand = parser.getParserCommand(text);
        AbstractHandler handlerForCommand = getHandlerForCommand(parserCommand.getCommand());
        String operationRes = handlerForCommand.operate(chatId.toString(), parserCommand, update);

        if(!"".equals(operationRes)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(operationRes);
            bot.sendQueue.add(sendMessage);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if(command == null) {
            LOG.warn("Null command receiver. This is a bad scenario.");
            return new DefaultHandler(bot);
        }
        switch (command) {
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(bot);
                LOG.info("Handler for command ["+command.toString()+"] is: "+notifyHandler.toString());
                return notifyHandler;
            case START:
            case HELP:
            case ID:
                SystemHandler systemHandler = new SystemHandler(bot);
                LOG.info("Handler for command ["+command.toString()+"] is: "+ systemHandler.toString());
                return systemHandler;
            default:
                LOG.info("Handler for command [" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot);
        }
    }

}