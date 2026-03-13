package checker.tbu;

import checker.html.BodyHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;

@Component
public final class ScheduledCheckTheFuture implements ScheduledCheck {

    @Autowired
    private Check check;

    @Value("${bot.url}")
    private String baseUrl;

    @Override
    @Scheduled(fixedRateString = "${bot.scheduler.interval}")
    public void scheduledCheck() throws Exception {
        String body = new BodyHtml(HttpClient.newHttpClient(), baseUrl).body();
        check.checkText(body);
        check.checkLength(body);
    }
}
