package org.vitaliistf.twowheels4u.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.vitaliistf.twowheels4u.dto.request.RentalRequestDto;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.MotorcycleService;
import org.vitaliistf.twowheels4u.service.RentalService;
import org.vitaliistf.twowheels4u.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @MockBean
    private UserService userService;

    @MockBean
    private MotorcycleService motorcycleService;


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void shouldCreateRental_ok() {
        User user = new User(1L, "user1@mail.com", "Vitalii", "Stefaniv", "123QWEasd",
                User.Role.MANAGER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                null, motorcycle, user);

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(rentalService.save(any(Rental.class))).thenReturn(rental);
        when((motorcycleService.findById(anyLong()))).thenReturn(motorcycle);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new RentalRequestDto(rental.getRentalDate(), rental.getReturnDate(),
                        rental.getMotorcycle().getId()))
                .when()
                .post("/rentals")
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void shouldReturnMotorcycle_ok() {
        User user = new User(1L, "user1@mail.com", "Vitalii", "Stefaniv", "123QWEasd",
                User.Role.MANAGER, 111111L);
        Motorcycle afterReturnMotorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        Rental afterReturnRental = new Rental(1L, LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), afterReturnMotorcycle, user);

        when(rentalService.returnRental(anyLong())).thenReturn(afterReturnRental);

        RestAssuredMockMvc.when()
                .put("/rentals/{id}/return", afterReturnMotorcycle.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void shouldGetRentalByUserIdAndIsAlive_ok() {
        User user = new User(1L, "user1@mail.com", "Vitalii", "Stefaniv", "123QWEasd",
                User.Role.MANAGER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), null, motorcycle, user);

        when(rentalService.findByIdAndIsActive(anyLong(), anyBoolean()))
                .thenReturn(List.of(rental));

        RestAssuredMockMvc.given()
                .queryParam("userId", user.getId())
                .queryParam("status", true)
                .when()
                .get("/rentals")
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void shouldReturnRentalById_ok() {

        User user = new User(1L, "user1@mail.com", "Vitalii", "Stefaniv", "123QWEasd",
                User.Role.MANAGER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), null, motorcycle, user);

        when(rentalService.findById(anyLong())).thenReturn(rental);

        RestAssuredMockMvc.when()
                .get("/rentals/{id}", rental.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void shouldReturnOwnRentals() {
        User user = new User(1L, "user1@mail.com", "Vitalii", "Stefaniv", "123QWEasd",
                User.Role.CUSTOMER, 111111L);
        Motorcycle motorcycle = new Motorcycle(1L, "Model", "Manufacturer", 6,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        Rental rental = new Rental(1L, LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), null, motorcycle, user);

        when(rentalService.findAll()).thenReturn(List.of(rental));

        RestAssuredMockMvc.
                when()
                .get("/rentals/my-rentals")
                .then()
                .statusCode(200);
    }
}
