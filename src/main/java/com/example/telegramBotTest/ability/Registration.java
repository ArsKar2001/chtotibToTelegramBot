package com.example.telegramBotTest.ability;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.handler.RegistrationHandler;
import com.example.telegramBotTest.interfaces.ButtonInterface;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

public class Registration implements Runnable, ButtonInterface {

    private static final Logger LOG = Logger.getLogger(Registration.class);
    public static Long CHAT_ID = null;
    public static RegStep step = RegStep.inputFIO;

    @Getter
    private final Bot bot;
    @Getter
    private String message;
    @Getter
    @Setter
    private static String fio;
    @Getter
    @Setter
    private static String group;

    public Registration(Bot bot, Long chatId) {
        this.bot = bot;
        CHAT_ID = chatId;
    }
    public Registration(Bot bot, String message) {
        this.bot = bot;
        this.message = message;
    }

    @Override
    public void run() {

        switch (step) {
            case inputFIO:
                bot.sendQueue.add(getInputFIOMessage());
                step = RegStep.inputGROUP;
                break;
            case inputGROUP:
                bot.sendQueue.add(getInputGroupMessage());
                step = RegStep.FINISHED;
                break;
//            case inputDATE:
//                bot.sendQueue.add(getInputDateMessage());
//                step = RegStep.inputGROUP;
//                break;
            case FINISHED:
                bot.sendQueue.add(getFinishMessage());
                LOG.info("[FINISHED] Registration to chatId: "+CHAT_ID);
                step = RegStep.NONE;
                CHAT_ID = null;
                break;
        }
    }

//    private Object getInputDateMessage() {
//    }

    private SendMessage getFinishMessage() {
        InlineKeyboardMarkup keyboardMarkup = newInlineKeyboardMarkup(
                Collections.singletonList(setInlineKeyboardButtons(
                        new InlineKeyboardButton().setText("ДА").setCallbackData("yes"),
                        new InlineKeyboardButton().setText("НЕТ").setCallbackData("no")
                )));
        return new SendMessage(CHAT_ID, "Спасибо!\n*ФИО:*" + getFio() + "\n*Группа:* " + getGroup() + "\nПодтверждаем?")
                .setReplyMarkup(keyboardMarkup);
    }

    private SendMessage getInputGroupMessage() {
        return new SendMessage(CHAT_ID, "Теперь введи свою группу...");

    }

    private SendMessage getInputFIOMessage() {
        return new SendMessage(CHAT_ID, "Давай мы тебя зарегистрируем :D\nВведи свои ФИО...");
    }

    private SendMessage getMessageExists() {
        return new SendMessage(CHAT_ID, "Похоже, что ты уже зарегистрирован :D");
    }
    
    enum RegStep {
        NONE,
        inputFIO,
        inputGROUP,
        inputDATE,
        FINISHED
    }
}
