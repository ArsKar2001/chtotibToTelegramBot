package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.commands.ChatCommands;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.essences.Process;
import com.example.telegramBotTest.interfaces.InlineKeyboardBuilder;
import com.example.telegramBotTest.service.Registration;
import lombok.var;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class DefaultHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        if (bot.chatIdList.contains(chatId)) {
            bot.processingRegQueue.add(update);
        }
        if(parserCommand.getChatCommands().equals(ChatCommands.UNKNOWN)) {
            bot.sendQueue.add(InlineKeyboardBuilder.getMessageMain(update, chatId));
            return "Я не знаю такой команды :(\n";
        }
        return "";
    }

    private void doSomeWithTextForUser(Update update) {

    }
}
