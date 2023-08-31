package org.vitaliistf.twowheels4u.controller;

import com.stripe.model.checkout.Session;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vitaliistf.twowheels4u.dto.request.PaymentRequestDto;
import org.vitaliistf.twowheels4u.dto.response.PaymentResponseDto;
import org.vitaliistf.twowheels4u.mapper.PaymentMapper;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.service.NotificationService;
import org.vitaliistf.twowheels4u.service.PaymentCalculationService;
import org.vitaliistf.twowheels4u.service.PaymentService;
import org.vitaliistf.twowheels4u.service.stripe.PaymentProviderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentProviderService paymentProviderService;
    private final PaymentService paymentService;
    private final PaymentCalculationService paymentCalculationService;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Endpoint for creating a payment session.")
    public PaymentResponseDto createPaymentSession(
            @Parameter(schema = @Schema(implementation = PaymentRequestDto.class))
            @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        Payment payment = paymentMapper.toModel(paymentRequestDto);

        BigDecimal moneyToPay = paymentCalculationService.calculatePaymentAmount(payment);
        BigDecimal moneyToFine = paymentCalculationService.calculateFineAmount(payment);

        payment.setPaymentAmount(moneyToPay);
        payment.setStatus(Payment.Status.PENDING);

        Session session = paymentProviderService.createPaymentSession(payment.getPaymentAmount(),
                moneyToFine, payment);
        payment.setSessionId(session.getId());
        payment.setUrl(session.getUrl());

        payment = paymentService.save(payment);

        notificationService.sendMessageToAdmin(
                "Started payment session for rental with id: " + paymentRequestDto.getRentalId()
        );
        notificationService.sendPaymentMessageToUser(payment,
                "You started payment session for rental with id: "
                        + paymentRequestDto.getRentalId()
        );

        return paymentMapper.toDto(payment);
    }

    @GetMapping
    @Operation(summary = "Endpoint for getting payments by user ID.",
            description = "Only managers are permitted.")
    public List<PaymentResponseDto> getByUserId(
            @Parameter(description = "User ID.", required = true)
            @RequestParam Long userId) {
        return paymentService.getByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/my-payments")
    @Operation(summary = "Endpoint for getting current user payments.")
    public List<PaymentResponseDto> findAllMyPayments(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();
        return paymentService.findAll().stream()
                .filter(p -> p.getRental().getUser().getEmail().equals(userEmail))
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/success/{id}")
    @Operation(description = "Endpoint that is visited after successful payment.")
    public PaymentResponseDto getSucceed(
            @Parameter(description = "Payment ID.", required = true)
            @PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        payment.setStatus(Payment.Status.PAID);

        notificationService.sendPaymentMessageToUser(payment,
                "You successfully paid for your rent: " + payment.getRental().toString()
        );

        return paymentMapper.toDto(paymentService.save(payment));
    }

    @GetMapping("/cancel/{id}")
    @Operation(description = "Endpoint that is visited after declining payment.")
    public PaymentResponseDto getCanceled(
            @Parameter(description = "Payment ID.", required = true)
            @PathVariable Long id) {
        notificationService.sendPaymentMessageToUser(paymentService.getById(id),
                "You cancel payment process. Please pay for your rent with id: " + id);

        return paymentMapper.toDto(paymentService.save(paymentService.getById(id)));
    }
}
