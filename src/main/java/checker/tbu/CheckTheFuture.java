package checker.tbu;

import checker.bot.SendingMessageTelegramLongPollingBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class CheckTheFuture implements Check {

    @Autowired
    SendingMessageTelegramLongPollingBot bot;

    private final Long[] chatId;

    private final String text;

    private LocalDateTime lastRun = LocalDateTime.now().minusDays(1);
    private long lastHtmlBodyLength = 0;

    public CheckTheFuture(SendingMessageTelegramLongPollingBot bot, @Value("${bot.chatId}") Long[] chatId, @Value("${bot.textToFind}") String text) {
        this.bot = bot;
        this.chatId = chatId;
        this.text = text;
    }

    @Override
    public void checkText(String body) {
        FoundTextOnWebPage foundTextOnWebPage = new FoundTextOnWebPageTheFuture(body, text);
        if (foundTextOnWebPage.textExists()) {
            bot.sendMessage("\uD83C\uDF89 Текст " + text + " на сайте ТБ найден! \uD83C\uDF89", chatId);
        } else if (Duration.between(lastRun, LocalDateTime.now()).toHours() >= 24) {
            bot.sendMessage("Текст " + text + " на сайте ТБ не найден.", chatId);
            lastRun = LocalDateTime.now();
        }
    }

    @Override
    public void checkLength(String body) {
        long len = body.length();
        if (lastHtmlBodyLength != 0 && lastHtmlBodyLength != len) {
            bot.sendMessage("\uD83C\uDF89 Длина сайта ТБ изменилась! \uD83C\uDF89", chatId);
        }
        lastHtmlBodyLength = len;
    }
}
