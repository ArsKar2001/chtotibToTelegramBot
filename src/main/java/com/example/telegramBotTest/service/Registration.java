package com.example.telegramBotTest.service;

import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.essences.Icon;
import com.example.telegramBotTest.essences.Role;
import com.example.telegramBotTest.essences.User;
import javafx.beans.binding.StringBinding;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Thread.sleep;

public class Registration implements Runnable {

    private static final Logger LOG = Logger.getLogger(Registration.class);
    private static final long WAIT_NEW_MESSAGE = 1000;
    public static RegStep step = RegStep.SELECT_ROLE;

    private final Bot bot;
    private final Long chatId;
    private boolean isActive;
    private final User user;

    public Registration(Bot bot, Long chatId) {
        this.bot = bot;
        this.chatId = chatId;
        isActive = true;
        user = new User();
    }

    @Override
    public void run() {
        try {
            LOG.info("[STARTED] Registration to chatId: "+chatId);
            Update update;
            while (isActive) {
                for (Object o = bot.processingQueue.poll(); o != null; o = bot.processingQueue.poll()) {
                    update = (Update) o;
                    if(bot.getChatId(update).equals(chatId)) nextStep(update);
                }
                try {
                    sleep(WAIT_NEW_MESSAGE);
                } catch (InterruptedException  e) {
                    disable();
                    LOG.error("Вышло прерывание.", e);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    void disable() {
        isActive = false;
    }

    private void nextStep(Update update) {
        switch (step) {
            case SELECT_ROLE:
                bot.sendQueue.add(getMessageSelectRole(update));
                step = RegStep.SELECT_COURSE;
                break;
            case SELECT_COURSE:
                String roleId = bot.getMessage(update);
                user.setRoleId(roleId);
                bot.sendQueue.add(getEditMessageTextSelectCourse(update));
                step = RegStep.SELECT_GROUP;
                break;
            case SELECT_GROUP:
                String groupYearIndex = bot.getMessage(update);
                user.setGroupYearIndex(groupYearIndex);
                bot.sendQueue.add(getData(update));
                disable();
                break;
            default:
                break;
        }
    }

    private SendMessage getMessageSelectRole(Update update) {
        String message = "Кто *Ты?*";
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(new InlineKeyboardButton().setText(Icon.MAN_STUDENT.get() + " Студент").setCallbackData(Role.STUDENT.toString()));
        buttonList1.add(new InlineKeyboardButton().setText(Icon.MAN_TEACHER.get() + " Учитель").setCallbackData(Role.TEACHER.toString()));

        lists.add(buttonList1);
        markup.setKeyboard(lists);

        return new SendMessage()
                .setChatId(chatId)
                .enableMarkdown(true)
                .setText(message)
                .setReplyMarkup(markup);
    }
    private EditMessageText getEditMessageTextSelectCourse(Update update) {
        String message = "Какой у тебя курс?";
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();

        buttonList1.add(new InlineKeyboardButton().setText("1").setCallbackData(String.valueOf(getAcademicYear())));
        buttonList1.add(new InlineKeyboardButton().setText("2").setCallbackData(String.valueOf(getAcademicYear() - 1)));
        buttonList2.add(new InlineKeyboardButton().setText("3").setCallbackData(String.valueOf(getAcademicYear() - 2)));
        buttonList2.add(new InlineKeyboardButton().setText("4").setCallbackData(String.valueOf(getAcademicYear() - 3)));

        lists.add(buttonList1);
        lists.add(buttonList2);

        markup.setKeyboard(lists);

        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(bot.getMessageId(update))
                .enableMarkdown(true)
                .setText(message)
                .setReplyMarkup(markup);
    }

    private EditMessageText getData(Update update) {
        String message = "*Роль*: " + user.getRoleId() +
                "\n*Год поступления*: " + user.getGroupYearIndex();
        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(bot.getMessageId(update))
                .enableMarkdown(true)
                .setText(message);
    }

    private int getAcademicYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int september_month = Month.SEPTEMBER.getValue();

        return (month < september_month) ? year - 1 : year;
    }

    enum RegStep {
        NONE,
        SELECT_ROLE,
        SELECT_COURSE,
        SELECT_GROUP
    }
}
