package checker.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public final class Bot extends TelegramLongPollingBot implements SendingMessageTelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.myChatId}")
    private Long myChatId;

    @Value("${bot.husbandsChatId}")
    private Long husbandsChatId;

    @Value("${bot.checkEnergy}")
    private Boolean checkEnergy;

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
            Message message = update.getMessage();
            System.out.println(message.getText());

            //TODO
            System.out.println(update.getMessage().getChatId());
        }
    }

}