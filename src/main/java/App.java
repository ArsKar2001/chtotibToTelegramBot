import com.example.telegramBotTest.bot.Bot;
import com.example.telegramBotTest.bot.BotConfig;
import com.example.telegramBotTest.service.MessageReceiver;
import com.example.telegramBotTest.service.MessageSender;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class App {

    private static final Logger LOG = Logger.getLogger(App.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;
    private static final String BOT_ADMIN = "321644283";

    public static void main(String[] args) {
        ApiContextInitializer.init();

        Bot bot = new Bot();

        MessageReceiver messageReceiver = new MessageReceiver(bot);
        MessageSender messageSender = new MessageSender(bot);

        bot.botConnection();

        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

//        sendStartReport(bot);
    }

//    private static void sendStartReport(Bot bot) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(BOT_ADMIN);
//        sendMessage.setText("Запустился");
//        bot.sendQueue.add(sendMessage);
//    }
}
