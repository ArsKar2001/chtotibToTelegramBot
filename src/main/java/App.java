import com.example.telegramBotTest2.bot.Bot;
import org.telegram.telegrambots.ApiContextInitializer;


public class App {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        Bot bot = new Bot();
        bot.botConnection();
    }
}
