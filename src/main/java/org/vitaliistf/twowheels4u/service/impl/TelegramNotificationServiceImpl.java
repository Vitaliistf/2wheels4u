package org.vitaliistf.twowheels4u.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vitaliistf.twowheels4u.bot.NotificationBot;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.MotorcycleService;
import org.vitaliistf.twowheels4u.service.NotificationService;
import org.vitaliistf.twowheels4u.service.RentalService;
import org.vitaliistf.twowheels4u.service.UserService;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl implements NotificationService {

    private final NotificationBot notificationBot;
    private final UserService userService;
    private final RentalService rentalService;
    private final MotorcycleService motorcycleService;

    @Override
    public void sendSuccessfulRentMessage(Rental rental) {
        if (rental.getUser().getTelegramId() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(rental.getUser().getTelegramId());
            sendMessage.setText(successfulRentMessage(rental));
            try {
                notificationBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Message is not sent.");
            }
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    @Override
    public void checkOverdueRentals() {
        LocalDateTime localDate = LocalDateTime.now();
        List<Rental> overdueRent = rentalService.findOverdueRentals(localDate);
        for (Rental rental : overdueRent) {
            if (rental.getUser().getTelegramId() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(rental.getUser().getTelegramId());
                sendMessage.setText(overdueRentMessage(rental, localDate));
                try {
                    notificationBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException("Message is not sent.");
                }
            }
        }
    }

    @Override
    public void sendMessageToAdmin(String message) {
        List<User> managers = userService.findByRoles(User.Role.MANAGER);
        sendMessageToUserBasedByRole(message, managers);
    }

    @Override
    public void sendPaymentMessageToUser(Payment payment, String message) {
        List<User> users = userService.findByRoles(User.Role.CUSTOMER);
        for (User user : users) {
            if (user.getTelegramId() != null
                    && rentalService.findById(payment.getRental().getId()).getUser().getEmail()
                    .equals(user.getEmail())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(user.getTelegramId());
                sendMessage.setText(message);
                try {
                    notificationBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException("Message is not sent.");
                }
            }
        }
    }

    private void sendMessageToUserBasedByRole(String message, List<User> users) {
        for (User user : users) {
            if (user.getTelegramId() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(user.getTelegramId());
                sendMessage.setText(message);
                try {
                    notificationBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException("Message is not sent.");
                }
            }
        }
    }

    private String successfulRentMessage(Rental rental) {
        Motorcycle motorcycle = motorcycleService.findById(rental.getMotorcycle().getId());
        return rental.getUser().getFirstName() + ", you have successfully rented: "
                + motorcycle.getModel() + " at " + rental.getRentalDate().toString()
                + ". Please return the motorcycle by " + rental.getReturnDate().toString()
                + ". Your daily fee is: " + motorcycle.getFee().toString();
    }

    private String overdueRentMessage(Rental rental, LocalDateTime dateTime) {
        return "You have overdue your rental with id: " + rental.getId()
                + ". Payment at " + dateTime.toString() + ". " + "Please, pay your fine!";
    }
}
