package org.vitaliistf.twowheels4u.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.vitaliistf.twowheels4u.dto.request.MotorcycleRequestDto;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.service.MotorcycleService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MotorcycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorcycleService motorcycleService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testAllMotorcycles() {
        List<Motorcycle> motorcycles = List.of(
                new Motorcycle(1L, "Model1", "Manufacturer1", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false),
                new Motorcycle(2L, "Model2", "Manufacturer2", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false),
                new Motorcycle(1L, "Model3", "Manufacturer4", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false)
        );
        Page<Motorcycle> motorcyclesPage = new PageImpl<>(motorcycles);
        when(motorcycleService.findAll(any(Pageable.class))).thenReturn(motorcyclesPage);

        RestAssuredMockMvc.when()
                .get("/motorcycles")
                .then()
                .statusCode(200)
                .body("size()", equalTo(3));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testAllMotorcyclesByParams() {
        Map<String, String> params = Map.of("inventory", "5");
        List<Motorcycle> motorcycles = List.of(
                new Motorcycle(1L, "Model1", "Manufacturer1", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false),
                new Motorcycle(2L, "Model2", "Manufacturer2", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false),
                new Motorcycle(1L, "Model3", "Manufacturer4", 5, Motorcycle.Type.STREET,
                        BigDecimal.valueOf(100), false)
        );
        when(motorcycleService.findAllByParams(params)).thenReturn(motorcycles);

        RestAssuredMockMvc.given()
                .queryParam("inventory", "5")
                .when()
                .get("/motorcycles/by-params")
                .then()
                .statusCode(200)
                .body("size()", equalTo(3));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testMotorcycleById() {
        Motorcycle motorcycle = new Motorcycle(1L, "Model1", "Manufacturer1", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);
        when(motorcycleService.findById(1L)).thenReturn(motorcycle);

        RestAssuredMockMvc.when()
                .get("/motorcycles/{id}", motorcycle.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testUpdateMotorcycle() {
        Motorcycle motorcycle = new Motorcycle(1L, "Model1", "Manufacturer1", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);
        when(motorcycleService.update(anyLong(), any(Motorcycle.class))).thenReturn(motorcycle);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new MotorcycleRequestDto(motorcycle.getModel(), motorcycle.getManufacturer(),
                        motorcycle.getFee(), motorcycle.getType().name(),
                        motorcycle.getInventory()))
                .when()
                .put("/motorcycles/{id}", motorcycle.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testDeleteMotorcycle() {
        Long motorcycleId = 1L;
        RestAssuredMockMvc.when()
                        .delete("/motorcycles/{id}", motorcycleId)
                                .then()
                                        .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testAddMotorcycleToInventory() {
        Motorcycle motorcycle = new Motorcycle(1L, "Model1", "Manufacturer1", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        when(motorcycleService.addMotorcycleToInventory(1L)).thenReturn(motorcycle);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .post("/motorcycles/add/{id}", 1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("model", equalTo("Model1"))
                .body("manufacturer", equalTo("Manufacturer1"))
                .body("inventory", equalTo(5));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testRemoveMotorcycleToInventory() {
        Motorcycle motorcycle = new Motorcycle(1L, "Model1", "Manufacturer1", 5,
                Motorcycle.Type.STREET, BigDecimal.valueOf(100), false);

        when(motorcycleService.removeMotorcycleFromInventory(1L)).thenReturn(motorcycle);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/motorcycles/remove/{id}", 1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("model", equalTo("Model1"))
                .body("manufacturer", equalTo("Manufacturer1"))
                .body("inventory", equalTo(5));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testCreateMotorcycle() {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setModel("Model");
        motorcycle.setManufacturer("Brand");
        motorcycle.setType(Motorcycle.Type.ENDURO);
        motorcycle.setFee(BigDecimal.valueOf(100));
        motorcycle.setInventory(5);
        motorcycle.setDeleted(false);

        when(motorcycleService.save(motorcycle)).thenReturn(
                new Motorcycle(1L, "Model1", "Manufacturer1", 5, Motorcycle.Type.STREET,
                BigDecimal.valueOf(100), false)
        );

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new MotorcycleRequestDto(motorcycle.getModel(), motorcycle.getManufacturer(),
                        motorcycle.getFee(), motorcycle.getType().name(),
                        motorcycle.getInventory()))
                .when()
                .post("/motorcycles")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }
}
