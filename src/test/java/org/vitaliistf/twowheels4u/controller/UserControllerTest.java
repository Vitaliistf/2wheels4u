package org.vitaliistf.twowheels4u.controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.vitaliistf.twowheels4u.config.SecurityConfig;
import org.vitaliistf.twowheels4u.dto.request.UserRequestDto;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserDetails userDetails;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testUpdateRole() {
        User userBeforeUpdate = new User(1L, "user1@mail.com", "firstName", "lastName",
                "123QWEasd", User.Role.CUSTOMER, 111111L);

        User userAfterUpdate = new User(1L, "user1@mail.com", "firstName", "lastName",
                "123QWEasd", User.Role.MANAGER,  111111L);
        when(userService.findById(1L)).thenReturn(userBeforeUpdate);
        when(userService.update(userBeforeUpdate)).thenReturn(userAfterUpdate);

        RestAssuredMockMvc.given()
                .queryParam("role", "MANAGER")
                .when()
                .put("/users/1/role")
                .then()
                .statusCode(200);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void testGetCurrentUser() {
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userService.findByEmail(anyString()))
                .thenReturn(new User(1L, "user1@mail.com", "firstName", "lastName",
                        "123QWEasd", User.Role.MANAGER, 111111L));

        RestAssuredMockMvc
                .when()
                .get("/users/me")
                .then()
                .statusCode(200);
    }


    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testUpdateInfo() {
        User userBeforeUpdate = new User(1L, "user1@mail.com", "firstName", "lastName",
                "123QWEasd", User.Role.CUSTOMER,111111L);

        User userAfterUpdate = new User(1L, "user1@mail.com", "firstName", "newLastName",
                "123QWEasd", User.Role.CUSTOMER,111111L);

        when(userService.findByEmail(anyString()))
                .thenReturn(userBeforeUpdate);
        when(userService.update(Mockito.any(User.class)))
                .thenReturn(userAfterUpdate);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(new UserRequestDto(userAfterUpdate.getEmail(),
                        userAfterUpdate.getPassword(), userAfterUpdate.getFirstName(),
                        userAfterUpdate.getLastName()))
                .when()
                .put("/users/me")
                .then()
                .statusCode(200);
    }
}
