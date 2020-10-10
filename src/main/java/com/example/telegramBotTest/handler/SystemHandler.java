package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.commands.ParserCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler extends AbstractHandler {

    private static final Logger LOG = Logger.getLogger(SystemHandler.class);
    private static final String END_LINE_STR = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParserCommand parserCommand, Update update) {
        Command command = parserCommand.getCommand();
        switch (command) {
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "Ваш TelegramId: "+update.getMessage().getFrom().getId();
        }
        return "";
    }

    private SendMessage getMessageHelp(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        String message = "*Это справочное сообщение.*" + END_LINE_STR + END_LINE_STR +
                "[/start](/start) - Показать сообщение приветствия" + END_LINE_STR +
                "[/help](/help) - Показать справочное сообщение" + END_LINE_STR +
                "[/id](/id) - Узнать свой TelegramId" + END_LINE_STR +
                "*/notify* _time-in-sec_ - Получать от меня уведомление по истечении указанного времени" + END_LINE_STR;

        sendMessage.setText(message);
        return sendMessage;
    }

    private SendMessage getMessageStart(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        String message = "Привет, "+bot.getUpdate().getMessage().getFrom().getUserName()+"! Меня зовут *" + bot.getBotUsername() + "*." + END_LINE_STR +
                "Моего создателя зовут - Сеня :D" + END_LINE_STR +
                "Я был создан для работы со студентами." + END_LINE_STR +
                "Чтобы посмотреть, что я умею - просто введи команду [/help](/help)" + END_LINE_STR;
        sendMessage.setText(message);
        return sendMessage;
    }
}
