package org.vitaliistf.twowheels4u.controller;

import com.stripe.model.checkout.Session;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.vitaliistf.twowheels4u.config.SecurityConfig;
import org.vitaliistf.twowheels4u.dto.request.PaymentRequestDto;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.PaymentCalculationService;
import org.vitaliistf.twowheels4u.service.PaymentService;
import org.vitaliistf.twowheels4u.service.RentalService;
import org.vitaliistf.twowheels4u.service.stripe.PaymentProviderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentCalculationService paymentCalculationService;

    @MockBean
    private PaymentProviderService paymentProviderService;

    @MockBean
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void tastCreatePayment() {
        User user = new User(1L, "user1@mail.com", "FirstName", "LastName",
                "123QWEasd", User.Role.CUSTOMER, 111111L);

        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setRentalDate(LocalDateTime.of(2023, 1, 1, 1,
                1, 1));
        rental.setReturnDate(LocalDateTime.of(2023, 1, 2, 1,
                1, 1));
        rental.setActualReturnDate(LocalDateTime.of(2023, 1, 3, 1,
                1, 1));
        rental.setMotorcycle(motorcycle);
        rental.setUser(user);

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setRental(rental);
        payment.setType(Payment.Type.PAYMENT);
        payment.setStatus(Payment.Status.PENDING);

        BigDecimal moneyToFine = BigDecimal.valueOf(100);
        BigDecimal moneyToPay = BigDecimal.valueOf(100);

        payment.setPaymentAmount(moneyToPay);

        when(paymentCalculationService.calculatePaymentAmount(payment))
                .thenReturn(moneyToPay);

        when(paymentCalculationService.calculateFineAmount(payment))
                .thenReturn(moneyToFine);

        when(rentalService.findById(anyLong())).thenReturn(rental);

        when(paymentService.save(any())).thenReturn(payment);

        Session sessionMock = Mockito.mock(Session.class);
        when(paymentProviderService.createPaymentSession(any(), any(), any())).thenReturn(sessionMock);
        when(sessionMock.getId()).thenReturn("id");
        when(sessionMock.getUrl()).thenReturn("google.com");

        RestAssuredMockMvc.given()
                .body(new PaymentRequestDto(payment.getRental().getId(), payment.getType()))
                .contentType(ContentType.JSON)
                .when()
                .post("/payments")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("status", equalTo(Payment.Status.PENDING.toString()))
                .body("amount", equalTo(100))
                .body("sessionId", equalTo(payment.getSessionId()));
    }

    @Test
    public void testGetSucceed() {
        User user = new User(1L, "nikitosik@i.ua", "FirstName", "LastName",
                "123QWEasd", User.Role.CUSTOMER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);
        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                null, motorcycle, user);

        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(Payment.Status.PAID);
        payment.setSessionId("exampleSessionId");
        payment.setUrl("exampleUrl");
        payment.setPaymentAmount(BigDecimal.TEN);
        payment.setType(Payment.Type.PAYMENT);
        payment.setRental(rental);

        when(rentalService.findById(anyLong())).thenReturn(rental);
        when(paymentService.getById(anyLong())).thenReturn(payment);
        when(paymentService.save(any(Payment.class))).thenReturn(payment);

        RestAssuredMockMvc.when()
                .get("/payments/success/{id}", paymentId)
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void shouldReturnOwnPayments_ok() {
        User user = new User();
        user.setRole(User.Role.CUSTOMER);
        user.setEmail("user1@mail.com");

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setUser(user);

        List<Payment> payments = List.of(
                new Payment(1L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING),
                new Payment(2L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING),
                new Payment(3L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING)
        );

        when(paymentService.findAll()).thenReturn(payments);

        RestAssuredMockMvc.when()
                .get("/payments/my-payments")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetCanceled() {
        User user = new User(1L, "nikitosik@i.ua", "FirstName", "LastName",
                "123QWEasd", User.Role.CUSTOMER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);
        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                null, motorcycle, user);

        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(Payment.Status.PAID);
        payment.setSessionId("exampleSessionId");
        payment.setUrl("exampleUrl");
        payment.setPaymentAmount(BigDecimal.TEN);
        payment.setType(Payment.Type.PAYMENT);
        payment.setRental(rental);

        when(rentalService.findById(anyLong())).thenReturn(rental);
        when(paymentService.getById(anyLong())).thenReturn(payment);
        when(paymentService.save(any(Payment.class))).thenReturn(payment);
        RestAssuredMockMvc.when()
                .get("/payments/cancel/{id}", paymentId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void shouldFindByUserId() {
        User user = new User();
        user.setId(1L);
        user.setRole(User.Role.CUSTOMER);
        user.setEmail("user1@mail.com");

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setUser(user);

        List<Payment> payments = List.of(
                new Payment(1L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING),
                new Payment(2L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING),
                new Payment(3L, rental, "some_session_url", "some_session_id", BigDecimal.TEN,
                        Payment.Type.PAYMENT, Payment.Status.PENDING)
        );

        when(paymentService.getByUserId(anyLong())).thenReturn(payments);

        RestAssuredMockMvc.given()
                .queryParam("userId", user.getId())
                .when()
                .get("/payments")
                .then()
                .statusCode(200);

    }
}
