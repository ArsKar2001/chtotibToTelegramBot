package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.ability.Registration;
import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.database.DBConnection;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegistrationHandler extends AbstractHandler {

    private static final Logger LOG = Logger.getLogger(RegistrationHandler.class);
    private static final short EXPECTATION_SEC = 30;

    @Getter
    private final DBConnection dbConnection;

    public RegistrationHandler(Bot bot) {
        super(bot);
        dbConnection = DBConnection.getInstance();
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        if(this.getDbConnection().isExists(chatId)) {
            LOG.info("[STARTED] Registration to chatId: "+chatId);
            Thread thread = new Thread(new Registration(bot, chatId));
            thread.setName("UserReg");
            thread.start();
            return "";
        }
        return "Похоже, что ты уже зарегистрирован в моей базе :D";
    }
}
