package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.service.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegistrationHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(RegistrationHandler.class);

    public RegistrationHandler(Bot bot) {
        super(bot);;
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        bot.chatIdList.add(chatId);
        bot.processingRegQueue.add(update);
        return "";
    }
}
