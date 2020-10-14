package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.ability.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultHandler extends AbstractHandler {

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {

        if(Registration.isActive) {
            if(bot.chatIdTextMap.containsKey(chatId)) {
                RegistrationHandler registrationHandler = new RegistrationHandler(bot);
                return registrationHandler.operate(chatId, parserCommand, update);
            }
        }

        return "Я не знаю такой команды :(\n" +
                "Введи команду /help, чтобы посмотреть, что я умею.";
    }
}
