import com.example.telegramBotTest2.bot.Bot;
import com.example.telegramBotTest2.service.MessageReceiver;
import com.example.telegramBotTest2.service.MessageSender;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;


public class App {

    private static final Logger LOG = Logger.getLogger(App.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    public static void main(String[] args) {
        ApiContextInitializer.init();

        Bot bot = new Bot();
        MessageReceiver messageReceiver = new MessageReceiver(bot);
        MessageSender messageSender = new MessageSender(bot);

        bot.botConnection();

        Thread threadReceiver = new Thread(messageReceiver);
        threadReceiver.setDaemon(true);
        threadReceiver.setName("MsgReceiver");
        threadReceiver.setPriority(PRIORITY_FOR_RECEIVER);
        threadReceiver.start();

        Thread threadSender = new Thread(messageSender);
        threadSender.setDaemon(true);
        threadSender.setName("MsgReceiver");
        threadSender.setPriority(PRIORITY_FOR_SENDER);
        threadSender.start();
    }
}
