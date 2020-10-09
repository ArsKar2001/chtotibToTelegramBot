package com.example.telegramBotTest2.commands;

import javafx.util.Pair;
import org.apache.log4j.Logger;

public class Parser {
    private static final Logger LOG = Logger.getLogger(Parser.class);
    private final String PREFIX_FOR_COMMAND = "/";
    private final String SEPARATOR_FOR_COMMAND = "@";
    private final String botName;

    public Parser(String botName) {
        this.botName = botName;
    }

    /**
     * @param text
     * @return
     */
    public ParserCommand getParserCommand(String text) {
        String trimStr = (text != null) ? text.trim() : "";
        ParserCommand command = new ParserCommand(Command.NONE, trimStr);

        if(trimStr.equals("")) return command;
        Pair<String, String> commandAndText = getSeparateCommand(text);
        if(isCommand(commandAndText.getKey())) {
            if(isCommandForMe(commandAndText.getKey())) {
                String commandFromParse = cutCommandFromFullText(commandAndText.getKey());
                Command commandFromText = getCommandFromText(commandFromParse);
                command.setText(commandAndText.getValue());
                command.setCommand(commandFromText);
            } else {
                command.setCommand(Command.NOTFORME);
                command.setText(commandAndText.getValue());
            }
        }
        return command;
    }

    /**
     * @param text
     * @return
     */
    private String cutCommandFromFullText(String text) {
        return (text.contains(SEPARATOR_FOR_COMMAND)) ?
                text.substring(1, text.indexOf(SEPARATOR_FOR_COMMAND)) :
                text.substring(1);
    }

    /**
     * @param text
     * @return
     */
    private Command getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            LOG.error("Не смог определить комманду: "+text+". " +
                    "Ошибка: "+e.getMessage());
        }
        return command;
    }

    /**
     * @param text
     * @return
     */
    private Pair<String, String> getSeparateCommand(String text) {
        Pair<String, String> stringPair;
        if(text.contains(" ")) {
            int indexCharSpace = text.indexOf(" ");
            stringPair = new Pair<>(text.substring(0, indexCharSpace), text.substring(indexCharSpace + 1));
        } else {
            stringPair = new Pair<>(text, "");
        }
        return stringPair;
    }

    /**
     * @param commandText
     * @return
     */
    private boolean isCommandForMe(String commandText) {
        if(commandText.contains(SEPARATOR_FOR_COMMAND)) {
            String botNameFromCommand = commandText.substring(commandText.indexOf(SEPARATOR_FOR_COMMAND + 1));
            return botName.equals(botNameFromCommand);
        }
        return true;
    }

    /**
     *
     * @param text
     * @return
     */
    private boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }
}
