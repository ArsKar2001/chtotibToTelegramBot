package com.example.telegramBotTest.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParserCommand {
    ChatCommands chatCommands = ChatCommands.NONE;
    String text = "";
}
