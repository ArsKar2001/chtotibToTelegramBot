package com.example.telegramBotTest.essences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long chatId;
    String groupYearIndex;
    Role role;

    public void setGroupYearIndex(String year) {
        int lastIndex = year.length();
        this.groupYearIndex = year.substring(lastIndex - 2, lastIndex);
    }
}

