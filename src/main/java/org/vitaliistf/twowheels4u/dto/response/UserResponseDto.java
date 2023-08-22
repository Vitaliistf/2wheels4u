package org.vitaliistf.twowheels4u.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
}
