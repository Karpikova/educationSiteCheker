package checker.bot;

import org.telegram.telegrambots.meta.generics.LongPollingBot;

public interface SendingMessageTelegramLongPollingBot extends LongPollingBot {

    void sendMessage(String msg, Long... chatIds);
}
