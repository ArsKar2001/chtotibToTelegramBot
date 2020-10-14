package com.example.telegramBotTest.handler;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.interfaces.ButtonInterface;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemHandler extends AbstractHandler implements ButtonInterface {

    private static final Logger LOG = Logger.getLogger(SystemHandler.class);
    private static final String END_LINE_STR = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(Long chatId, ParserCommand parserCommand, Update update) {
        Command command = parserCommand.getCommand();
        switch (command) {
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                int telegramId = bot.getTelegramId(update);
                return "Ваш TelegramId: "+telegramId;
        }
        return "";
    }

    private SendMessage getMessageHelp(Long chatId) {
        String message = "*Это справочное сообщение.*";
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>(this.setInlineKeyboardButtons(
                new InlineKeyboardButton().setText("Старт").setCallbackData("/start"),
                new InlineKeyboardButton().setText("Справка").setCallbackData("/help"),
                new InlineKeyboardButton().setText("Ваш TelegramId").setCallbackData("/id")
        ));
        InlineKeyboardMarkup keyboardMarkup = this.newInlineKeyboardMarkup(Collections.singletonList(keyboardButtons));
        return new SendMessage().setChatId(chatId).enableMarkdown(true).setText(message).setReplyMarkup(keyboardMarkup);
    }

    private SendMessage getMessageStart(Long chatId) {
        String message = "Привет! Меня зовут *" + bot.getBotUsername() + "*." + END_LINE_STR +
                "Моего создателя зовут - Сеня :D" + END_LINE_STR +
                "Я был создан для работы со студентами.";
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>(this.setInlineKeyboardButtons(
                new InlineKeyboardButton().setText("Регистрация").setCallbackData("/reg"),
                new InlineKeyboardButton().setText("Справка").setCallbackData("/help")
        ));
        InlineKeyboardMarkup keyboardMarkup = this.newInlineKeyboardMarkup(Collections.singletonList(keyboardButtons));
        return new SendMessage().setChatId(chatId).enableMarkdown(true).setText(message).setReplyMarkup(keyboardMarkup);
    }
}
