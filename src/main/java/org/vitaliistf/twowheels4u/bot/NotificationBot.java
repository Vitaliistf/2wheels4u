package org.vitaliistf.twowheels4u.bot;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.repository.UserRepository;

@Component
public class NotificationBot extends TelegramLongPollingBot {
    private static final String FIRST_MESSAGE = "/start";

    private final UserRepository userRepository;

    private final TelegramBotsApi telegramBotsApi;

    @Value("${telegram-bot-name}")
    private String botName;

    public NotificationBot(@Value("${telegram-bot-token}") String botToken,
                           UserRepository userRepository, TelegramBotsApi telegramBotsApi) {
        super(botToken);
        this.userRepository = userRepository;
        this.telegramBotsApi = telegramBotsApi;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals(FIRST_MESSAGE)) {
                greetMessage(chatId, update.getMessage().getChat().getFirstName());
            } else {
                Optional<User> userByEmail = userRepository.findByEmail(messageText);
                if (userByEmail.isPresent()) {
                    User user = userByEmail.get();
                    user.setTelegramId(chatId);
                    userRepository.save(user);
                    successMessage(chatId);
                } else {
                    failureMessage(chatId);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Cannot sent message.");
        }
    }

    private void greetMessage(Long chatId, String name) {
        String text = "Hello, " + name + ", please send your email.";
        sendMessage(chatId, text);
    }

    private void failureMessage(Long chatId) {
        String text = "User with this email doesn't exist, please check your credentials.";
        sendMessage(chatId, text);
    }

    private void successMessage(Long chatId) {
        String text = "You are successfully sync with your account.";
        sendMessage(chatId, text);
    }
}
