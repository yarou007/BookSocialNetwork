package com.yarou.book.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class AuthenticationRequest {

    @Email(message = "email is not formatted")
    @NotEmpty(message = "email is mandatory")
    @NotBlank(message = "email no space allowed")
    private String email;
    @NotEmpty(message = "password is mandatory")
    @NotBlank(message = "password no space allowed")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

}
