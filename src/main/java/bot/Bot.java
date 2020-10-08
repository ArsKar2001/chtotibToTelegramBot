package bot;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOG = Logger.getLogger(Bot.class);
    private static final short PAUSE = 10000;

    @Override
    public void onUpdateReceived(Update update) {

        LOG.debug("onUpdateReceived");
        LOG.debug("Receive new Update. updateID: " + update.getUpdateId());

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        if (inputText.startsWith("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Hello. This is start message");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.USERNAME_BOT;
    }

    @Override
    public String getBotToken() {
        return BotConfig.TOKEN_BOT;
    }

    public void botConnection() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            LOG.info("Bot started!");
        } catch (TelegramApiRequestException e) {
            LOG.error("Не может соедениться. Ожидание "+PAUSE / 1000+" сек. и выход\n" +
                    "Ошибка: "+e.getMessage());
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException interruptedException) {
                LOG.error(interruptedException.getMessage());
                return;
            }
            botConnection();
        }
    }
}
