package com.example.telegramBotTest.ability;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.commands.Command;
import com.example.telegramBotTest.commands.ParserCommand;
import com.example.telegramBotTest.interfaces.ButtonInterface;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

public class Registration implements Runnable, ButtonInterface {

    private static final Logger LOG = Logger.getLogger(Registration.class);
    public static boolean isActive = false;
    public static RegStep step = RegStep.inputFIO;

    @Getter
    private final Bot bot;
    @Getter
    private final Long chatId;
    @Getter
    @Setter
    private static String fio;
    @Getter
    @Setter
    private static String group;
    @Getter
    private final String message;
    @Getter
    private final ParserCommand parser;

    public Registration(Bot bot, Update update, ParserCommand parserCommand) {
        this.bot = bot;
        this.chatId = bot.getChatId(update);
        this.message = bot.getMessage(update);
        this.parser = parserCommand;
        LOG.debug("Полученные данные: "+this.message);
    }

    @Override
    public void run() {

        isStep();

        switch (step) {
            case inputFIO:
                isActive = true;
                bot.sendQueue.add(getInputFIOMessage());
                step = RegStep.inputGROUP;
                break;
            case inputGROUP:
                fio = message;
                bot.sendQueue.add(getInputGroupMessage());
                step = RegStep.CONFIRM;
                break;
            case inputDATE:
                break;
            case CONFIRM:
                group = message;
                bot.sendQueue.add(getFinishMessage());
                break;
            case CANCEL:
                step = RegStep.NONE;
                isActive = false;
                break;
            case OK:
                isActive = false;
                break;
            default:
                 break;
        }
    }

    private void isStep() {
        if(!parser.getCommand().equals(Command.NONE)) {
            if (!parser.getText().equals(""))
                step = RegStep.valueOf(parser.getText().toUpperCase().trim());
        }
    }

//    private Object getInputDateMessage() {
//    }

    private SendMessage getFinishMessage() {
        InlineKeyboardMarkup keyboardMarkup = newInlineKeyboardMarkup(
                Collections.singletonList(setInlineKeyboardButtons(
                        new InlineKeyboardButton().setText("Да").setCallbackData("/reg yes"),
                        new InlineKeyboardButton().setText("Заново").setCallbackData("/reg"),
                        new InlineKeyboardButton().setText("Выход").setCallbackData("/reg cancel")
                )));
        return new SendMessage(chatId, "Спасибо!\n*ФИО:* " + fio + "\n*Группа:* " + group + "\nПодтверждаем?")
                .setReplyMarkup(keyboardMarkup).enableMarkdown(true);
    }

    private SendMessage getInputGroupMessage() {
        return new SendMessage(chatId, "Теперь введи свою группу...").enableMarkdown(true);

    }

    private SendMessage getInputFIOMessage() {
        return new SendMessage(chatId, "Давай мы тебя зарегистрируем :D\nВведи свои ФИО...").enableMarkdown(true);
    }

    private SendMessage getMessageExists() {
        return new SendMessage(chatId, "Похоже, что ты уже зарегистрирован :D").enableMarkdown(true);
    }
    
    enum RegStep {
        NONE,
        inputFIO,
        inputGROUP,
        inputDATE,
        CONFIRM,
        CANCEL,
        OK
    }
}
