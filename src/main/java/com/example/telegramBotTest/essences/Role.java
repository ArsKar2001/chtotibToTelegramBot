package com.example.telegramBotTest.essences;

public enum Role{
    STUDENT(100),
    TEACHER(101);

    private final int values;

    Role(int values) {
        this.values = values;
    }

    public int getId() {
        return values;
    }

    @Override
    public String toString() {
        return String.valueOf(values);
    }
}
