package com.example.telegramBotTest.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {
    public static SendMessage getMessageMain(Update update, Long chatId) {
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
                .setChatId(chatId)
                .enableMarkdown(true)
                .setText(message)
                .setReplyMarkup(markup);
    }
}
