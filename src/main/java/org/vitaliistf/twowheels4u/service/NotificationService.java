package org.vitaliistf.twowheels4u.service;

import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.model.Rental;

public interface NotificationService {

    void sendSuccessfulRentMessage(Rental rental);

    void sendMessageToAdmin(String message);

    void sendPaymentMessageToUser(Payment payment, String message);

    void checkOverdueRentals();
}
