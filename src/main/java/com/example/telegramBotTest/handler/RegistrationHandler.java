package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.ability.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegistrationHandler extends AbstractHandler {

    RegistrationHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParserCommand parserCommand, Update update) {

        Thread thread = new Thread(new Registration(bot, chatId));
        thread.setName("UserRegistration");
        thread.start();

        return "";
    }
}
