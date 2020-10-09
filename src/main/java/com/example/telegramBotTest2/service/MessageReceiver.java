package com.example.telegramBotTest2.service;

import com.example.telegramBotTest2.bot.Bot;
import com.example.telegramBotTest2.commands.Command;
import com.example.telegramBotTest2.handler.AbstractHandler;
import org.apache.log4j.Logger;


public class MessageReceiver implements Runnable {

    private static final Logger LOG = Logger.getLogger(MessageReceiver.class);
    private static final short SLEEP = 1000;
    private Bot bot;

    public MessageReceiver(Bot bot) {
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
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOG.info("[STARTED] MessageReceiver. Bot class - " + bot);
        try {
            while (true) {
                for (Object o : bot.receiveQueue) {
                    LOG.debug("Новый объект для анализа "+o.toString());
                    analyze(o);
                }
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException  e) {
                    LOG.error("Вышло прерывание. Выход ", e);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * Проверяет тип объекта
     * @param o
     */
    private void analyze(Object o) {
//        try {
//            Command command = (Command) o;
//            switch (command) {
//                case NOTFORME:
//
//                    break;
//                case NOTIFY:
//                    break;
//                case START:
//                    break;
//                case HELP:
//                    break;
//                case ID:
//                    break;
//                default:
//                    break;
//            }
//        }
    }

//    private AbstractHandler abstractHandler
}
