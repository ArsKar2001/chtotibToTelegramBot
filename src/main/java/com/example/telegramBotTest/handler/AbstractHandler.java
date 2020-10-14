package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
    Bot bot;

    AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(Long chatId, ParserCommand parserCommand, Update update);

    public String getTelegramId(Update update) {
        if(update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getFrom().getId().toString();
        return update.getMessage().getFrom().getId().toString();
    }

}
