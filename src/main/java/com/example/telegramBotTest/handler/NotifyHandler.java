package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.ability.Notify;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotifyHandler extends AbstractHandler {

    private static final Logger LOG = Logger.getLogger(NotifyHandler.class);
    private static final short PAUSE_MS = 1000;
    private static final String WRONG_MESSAGE = "Неправельный ввод. Число должно быть целым и больше 0";

    public NotifyHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParserCommand parserCommand, Update update) {
        String text = parserCommand.getText();
        long timeInSec;

        if(text.equals("")) return "Вы должны указать время задержки. Например: /notify 30";
        try {
            timeInSec = Long.parseLong(text.trim());
        } catch (NumberFormatException e) {
            return WRONG_MESSAGE;
        }
        if(timeInSec > 0) {
            Thread thread = new Thread(new Notify(bot, chatId, timeInSec * PAUSE_MS));
            thread.start();
        } else {
            return WRONG_MESSAGE;
        }
        return "";
    }
}
