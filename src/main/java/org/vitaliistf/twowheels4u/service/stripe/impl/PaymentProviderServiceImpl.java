package org.vitaliistf.twowheels4u.service.stripe.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.service.PaymentService;
import org.vitaliistf.twowheels4u.service.stripe.PaymentProviderService;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderService {
    private static final String USD = "usd";
    private static final Long QUANTITY = 1L;
    @Value("${stripe-secret}")
    private String secretKey;
    @Value("${stripe-domen}")
    private String url;
    private final PaymentService paymentService;

    public Session createPaymentSession(BigDecimal payment, BigDecimal fine,
                                        Payment paymentObject) {
        Stripe.apiKey = secretKey;
        Payment savedPayment = paymentService.save(paymentObject);
        Long paymentId = savedPayment.getId();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(USD)
                                .setUnitAmount((long) (payment.doubleValue() * 100))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData
                                                .ProductData
                                                .builder()
                                                .setName(paymentObject.getType()
                                                        .name())
                                                .build()
                                )
                                .build()
                )
                .build();

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(url + "/payments/success/" + paymentId)
                .setCancelUrl(url + "/payments/cancel/" + paymentId)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(lineItem);

        if (fine.doubleValue() > 0) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(QUANTITY)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(USD)
                                            .setUnitAmount((long) (fine.doubleValue() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData
                                                            .ProductData.builder()
                                                            .setName(Payment.Type.FINE.name())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }
        SessionCreateParams params = paramsBuilder.build();
        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Cannot create session with parameters: " + params, e);
        }
    }
}
