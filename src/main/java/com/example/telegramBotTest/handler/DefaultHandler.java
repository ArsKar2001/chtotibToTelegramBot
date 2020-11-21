package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.service.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import lombok.var;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class DefaultHandler extends AbstractHandler {

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        if(!parserCommand.getCommand().equals(Command.UNKNOWN)) {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for (var thread : threadSet) {
                if (thread.getName().equals("Reg_" + chatId)) {
                    bot.processingQueue.add(update);
                    return "";
                }
            }
        }
        return "Я не знаю такой команды :(\n" +
                "Введи команду /help, чтобы посмотреть, что я умею.";
    }
}
