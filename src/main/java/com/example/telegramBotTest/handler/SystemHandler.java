package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.ChatCommands;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.database.ProcedureAndFunction;
import com.example.telegramBotTest.interfaces.InlineKeyboardBuilder;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        ChatCommands chatCommands = parserCommand.getChatCommands();
        LOG.info("Handler for command ["+ chatCommands.toString()+"] is: "+this.toString());
        switch (chatCommands) {
            case MAIN:
                bot.sendQueue.add(InlineKeyboardBuilder.getMessageMain(update, chatId));
            case START:
                bot.sendQueue.add(getMessageHello(update));
                if(!ProcedureAndFunction.isExists(chatId))  bot.sendQueue.add(InlineKeyboardBuilder.getMessageMain(update, chatId));
                else {
                    bot.chatIdList.add(chatId);
                    bot.processingRegQueue.add(update);
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
