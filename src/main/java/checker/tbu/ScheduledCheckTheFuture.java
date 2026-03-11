package checker.tbu;

import checker.bot.SendingMessageTelegramLongPollingBot;
import checker.html.BodyHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public final class ScheduledCheckTheFuture implements ScheduledCheck {

    @Autowired
    SendingMessageTelegramLongPollingBot bot;

    @Value("${bot.url}")
    private String BASE_URL;

    @Value("${bot.chatId}")
    private Long[] chatId;

    @Value("${bot.textToFind}")
    private String text;

    @Value("${bot.checkTheFuture}")
    private Boolean checkTheFuture;

    private LocalDateTime lastRun = LocalDateTime.now().minusDays(1);
    private long lastHtmlBodyLength = 0;

    @Override
    @Scheduled(fixedRateString = "${bot.scheduler.interval}")
    public void scheduledCheck() throws Exception {
        if (checkTheFuture) {
            String body = new BodyHtml(HttpClient.newHttpClient(), BASE_URL).body();
            checkText(body);
            checkLength(body);
        }
    }

    private void checkText(String body) {
        FoundTextOnWebPage foundTextOnWebPage = new FoundTextOnWebPageTheFuture(body, text);
        if (foundTextOnWebPage.textExists()) {
            bot.sendMessage("\uD83C\uDF89 Текст " + text + " на сайте ТБ найден! \uD83C\uDF89", chatId);
        } else if (Duration.between(lastRun, LocalDateTime.now()).toHours() >= 24) {
            bot.sendMessage("Текст " + text + " на сайте ТБ не найден.", chatId);
            lastRun = LocalDateTime.now();
        }
    }

    private void checkLength(String body) {
        long len = body.length();
        if (lastHtmlBodyLength !=0 && lastHtmlBodyLength != len) {
            bot.sendMessage("\uD83C\uDF89 Длина сайта ТБ изменилась! \uD83C\uDF89", chatId);
        }
        lastHtmlBodyLength = len;
    }
}
