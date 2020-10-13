package com.example.telegramBotTest.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ButtonInterface {
    default InlineKeyboardMarkup newInlineKeyboardMarkup(List<List<InlineKeyboardButton>> lists) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(lists);
        return keyboardMarkup;
    }
    default List<InlineKeyboardButton> setInlineKeyboardButtons(InlineKeyboardButton... buttons) {
        return new ArrayList<>(Arrays.asList(buttons));
    }
}
