package com.yarou.book.auth;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "first name is mandatory")
    @NotBlank(message = "first name no space allowed")

    private String firstname;
    @NotEmpty(message = "last name is mandatory")
    @NotBlank(message = "last name no space allowed")
    private String lastname;
    @Email(message = "email is not formatted")
    @NotEmpty(message = "email is mandatory")
    @NotBlank(message = "email no space allowed")
    private String email;
    @NotEmpty(message = "password is mandatory")
    @NotBlank(message = "password no space allowed")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

}
