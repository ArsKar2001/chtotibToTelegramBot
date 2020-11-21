package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.database.ProcedureAndFunction;
import com.example.telegramBotTest.essences.Icon;
import com.example.telegramBotTest.essences.Role;
import com.example.telegramBotTest.service.Registration;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SystemHandler extends AbstractHandler {

    private static final Logger LOG = Logger.getLogger(SystemHandler.class);
    private static final String END_LINE_STR = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        Command command = parserCommand.getCommand();
        switch (command) {
            case MAIN:
                bot.sendQueue.add(getMessageMain(update));
            case START:
                bot.sendQueue.add(getMessageHello(update));
                if(!ProcedureAndFunction.isExists(chatId)) {
                    bot.sendQueue.add(getMessageMain(update));
                } else {
                    RegistrationHandler registrationHandler = new RegistrationHandler(bot);
                    return registrationHandler.operate(chatId, parserCommand, update);
                }
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(update));
                break;
            case ID:
                int telegramId = bot.getTelegramId(update);
                return "Ваш TelegramId: "+telegramId;
        }
        return "";
    }

    private SendMessage getMessageHelp(Update update) {
        String message = "*Это все лишь справочное сообщение (еще в разработке).*\n1 - ваш TelegramId\n2 - вернуться назад";
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();

        buttonList1.add(new InlineKeyboardButton().setText("1").setCallbackData("/id"));
        buttonList1.add(new InlineKeyboardButton().setText("2").setCallbackData("/main"));

        lists.add(buttonList1);
        markup.setKeyboard(lists);

        return new SendMessage()
                .setChatId(bot.getChatId(update))
                .enableMarkdown(true)
                .setText(message)
                .setReplyMarkup(markup);
    }

    private SendMessage getMessageMain(Update update) {
        String message = "Вот, что я умею (еще в разработке):\n1 - расписание группы;\n2 - расписание занятий на завтра;\n3 - справка.";
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();

        buttonList1.add(new InlineKeyboardButton().setText("1").setCallbackData("/start")); //временная заглушка
        buttonList1.add(new InlineKeyboardButton().setText("2").setCallbackData("/start")); //временная заглушка
        buttonList1.add(new InlineKeyboardButton().setText("3").setCallbackData("/help"));
        lists.add(buttonList1);
        markup.setKeyboard(lists);

        return new SendMessage()
                .setChatId(bot.getChatId(update))
                .enableMarkdown(true)
                .setText(message)
                .setReplyMarkup(markup);
    }

    private SendMessage getMessageHello(Update update) {
        String message = "Привет! Меня зовут *" + bot.getBotUsername() + "*." + END_LINE_STR +
                "Моего создателя зовут - Сеня :D" + END_LINE_STR +
                "Я был создан для работы со студентами и преподавателями в ЧТОТиБ.";
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .enableMarkdown(true)
                .setText(message);
    }
}
