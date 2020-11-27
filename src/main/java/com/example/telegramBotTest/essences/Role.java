package com.example.telegramBotTest.essences;

import lombok.Getter;

@Getter
public enum Role{
    STUDENT(100),
    TEACHER(101);

    private final int values;

    Role(int values) {
        this.values = values;
    }
}
