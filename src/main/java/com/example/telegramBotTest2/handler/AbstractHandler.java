package com.example.telegramBotTest2.handler;

import com.example.telegramBotTest2.bot.Bot;
import com.example.telegramBotTest2.commands.ParserCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
    Bot bot;

    AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(String chatId, ParserCommand parserCommand, Update update);
}
