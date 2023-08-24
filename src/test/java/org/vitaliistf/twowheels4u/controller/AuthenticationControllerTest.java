package org.vitaliistf.twowheels4u.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.vitaliistf.twowheels4u.dto.request.UserLoginRequestDto;
import org.vitaliistf.twowheels4u.dto.request.UserRegisterRequestDto;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.security.AuthenticationService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "user1@mail.com";
    private static final String PASSWORD = "123QWEasd";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;
    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setLastName(LAST_NAME);
        user.setPassword(PASSWORD);
        user.setId(ID);
        user.setFirstName(FIRST_NAME);
        user.setRole(User.Role.CUSTOMER);

        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void testRegister() {
        when(authenticationService.register(eq(user.getEmail()), eq(user.getPassword()),
                eq(user.getFirstName()), eq(user.getLastName()))).thenReturn(user)
                .thenReturn(user);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new UserRegisterRequestDto(user.getEmail(), user.getPassword(),
                        user.getPassword(), user.getFirstName(), user.getLastName()))
                .when()
                .post("/register")
                .then()
                .statusCode(200);
    }

    @Test
    public void testRegisterWrongInput() {
        when(authenticationService.register(anyString(), anyString(),
                anyString(), anyString())).thenReturn(user);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new UserRegisterRequestDto(user.getEmail(), user.getPassword(),
                        user.getPassword() + " ", user.getFirstName(),
                        user.getLastName() ))
                .when()
                .post("/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void testLogin() {
        when(authenticationService.login(anyString(), anyString()))
                .thenReturn(user);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new UserLoginRequestDto(user.getEmail(), user.getPassword()))
                .when()
                .post("/login")
                .then()
                .statusCode(200);
    }
}
