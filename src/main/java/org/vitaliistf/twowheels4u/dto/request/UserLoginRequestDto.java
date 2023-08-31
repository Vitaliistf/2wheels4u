package org.vitaliistf.twowheels4u.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "Email cannot be empty or null.")
    private String email;

    @NotBlank(message = "Password cannot be empty or null.")
    private String password;

}
