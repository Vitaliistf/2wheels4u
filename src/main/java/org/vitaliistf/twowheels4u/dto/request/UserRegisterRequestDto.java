package org.vitaliistf.twowheels4u.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.vitaliistf.twowheels4u.util.validation.PasswordMatch;
import org.vitaliistf.twowheels4u.util.validation.ValidEmail;
import org.vitaliistf.twowheels4u.util.validation.ValidPassword;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@PasswordMatch(
        password = "password",
        passwordConfirmation = "repeatPassword"
)
public class UserRegisterRequestDto {

    @NotBlank(message = "Email cannot be empty or null.")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password cannot be empty or null.")
    @ValidPassword
    private String password;

    private String repeatPassword;

    @NotBlank(message = "First name cannot be empty or null.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty or null.")
    private String lastName;
}
