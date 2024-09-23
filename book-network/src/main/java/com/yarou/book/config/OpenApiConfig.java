package com.yarou.book.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "AliBou",
                        email = "yacine.hammi@esen.tn",
                        url = "https://aliboucoding.com/courses"
                ),
                description = "Open API Documentation for spring security",
                title = "Open API Specification",
                version = "1.0",
                license = @License(
                        name ="Licence name",
                        url="https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "local ENV ",
                        url = "http://localhost:8088/api/v1/"
                ),
                @Server(
                        description = "Prod env",
                        url = "https://aliboucoding.com/courses"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
               )
       }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "Jwt Auth descritpion",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
