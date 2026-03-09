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
public class ScheduledCheckTheFuture implements ScheduledCheck {

    private final static String BASE_URL = "https://xn--80acgdf0a1ag2aob6b6a.xn--p1ai/irkutsk/";

    @Autowired
    SendingMessageTelegramLongPollingBot bot;

    @Value("${bot.myChatId}")
    private Long myChatId;

    @Value("${bot.textToFind}")
    private String text;

    @Value("${bot.checkTheFuture}")
    private Boolean checkTheFuture;

    private LocalDateTime lastRun = LocalDateTime.now().minusDays(1);

    @Override
    @Scheduled(fixedRateString = "${bot.scheduler.interval:900000}")
    public void scheduledCheck() throws Exception {
        if (checkTheFuture) {
            String body = new BodyHtml(HttpClient.newHttpClient(), BASE_URL).body();
            checker.tbu.FoundTextOnWebPage foundTextOnWebPage = new checker.tbu.FoundTextOnWebPageTheFuture(body, text);
            if (foundTextOnWebPage.textExists()) {
                bot.sendMessage("\uD83C\uDF89 Текст " + text + " на сайте Точки Будущего найден! \uD83C\uDF89", myChatId);
            } else if (Duration.between(lastRun, LocalDateTime.now()).toHours() >= 24) {
                bot.sendMessage("Набор в Точку Будущего пока не открыт", myChatId);
                lastRun = LocalDateTime.now();
            }
        }
    }
}
