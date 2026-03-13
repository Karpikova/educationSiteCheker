package checker.bot;

import checker.html.BodyHtml;
import checker.tbu.Check;
import checker.tbu.CheckTheFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.http.HttpClient;

@Component
public final class Bot extends TelegramLongPollingBot implements SendingMessageTelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.url}")
    private String baseUrl;

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void sendMessage(String msg, Long... chatIds) {
        SendMessage response = new SendMessage();
        for (Long chatId : chatIds) {
            response.setChatId(String.valueOf(chatId));
            response.setText(msg);
            try {
                System.out.println(chatId);
                execute(response);
            } catch (TelegramApiException e) {
                System.out.println("Failed to send message");
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Check ch = new CheckTheFuture(this, new Long[]{update.getMessage().getChatId()},
                    update.getMessage().getText());
            try {
                String body = new BodyHtml(HttpClient.newHttpClient(), baseUrl).body();
                ch.checkText(body);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}