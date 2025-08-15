package com.salenhard.books.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSuperClassDto {
    @Schema(description = "User id", example = "1")
    Long id;
    @NotBlank(message = "Username is mandatory")
    @Schema(description = "User username", example = "user")
    String userName;
    @Schema(description = "User password", example = "12345678")
    @NotBlank(message = "Password is mandatory")
    String password;
}
