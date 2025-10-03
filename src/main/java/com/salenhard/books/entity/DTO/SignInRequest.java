package com.salenhard.books.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Sign in request")
public class SignInRequest {
    @Schema(description = "Users username", example = "user")
    @NotBlank(message = "Username is mandatory")
    String username;
    @Schema(description = "Users password", example = "12345678Aa")
    @NotBlank(message = "Password is mandatory")
    String password;
}
