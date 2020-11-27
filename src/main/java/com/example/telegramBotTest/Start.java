package com.example.telegramBotTest;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.database.DB;
import com.example.telegramBotTest.service.MessageReceiver;
import com.example.telegramBotTest.service.MessageSender;
import com.example.telegramBotTest.service.Registration;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;


public class Start {
    private static final Logger LOG = Logger.getLogger(Start.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_REG = 2;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    public static void main(String[] args) {
        ApiContextInitializer.init();

        Bot bot = new Bot();
        DB db = new DB();

        MessageReceiver messageReceiver = new MessageReceiver(bot);
        MessageSender messageSender = new MessageSender(bot);
        Registration registration = new Registration(bot);

        bot.botConnection();
        db.connectingDB();

        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

        Thread reg = new Thread(registration);
        reg.setName("RegUser");
        reg.setPriority(PRIORITY_FOR_REG);
        reg.setDaemon(true);
        reg.start();

//        sendStartReport(bot);
    }

//    private static void sendStartReport(Bot bot) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(BOT_ADMIN);
//        sendMessage.setText("Запустился");
//        bot.sendQueue.add(sendMessage);
//    }
}
