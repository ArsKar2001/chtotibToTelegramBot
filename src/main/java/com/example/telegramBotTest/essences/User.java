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
    int roleId;

    public void setRoleId(String roleId) {
        this.roleId = Integer.parseInt(roleId);
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setGroupYearIndex(String year) {
        int lastIndex = year.length();
        this.groupYearIndex = year.substring(lastIndex - 2, lastIndex);
    }
}

