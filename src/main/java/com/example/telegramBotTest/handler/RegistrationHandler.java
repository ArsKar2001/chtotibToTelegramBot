package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.service.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.database.ProcedureAndFunction;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegistrationHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(RegistrationHandler.class);


    public RegistrationHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        bot.processingQueue.add(update);
        Thread thread = new Thread(new Registration(bot, chatId));
        thread.setName("Reg_"+chatId);
        thread.start();
        return "";
    }
}
