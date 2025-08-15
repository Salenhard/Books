package com.salenhard.books.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNamesDto {
    @Schema(description = "User name", example = "John")
    @NotBlank(message = "Name is mandatory")
    String name;
    @Schema(description = "User middle name", example = "Rick")
    @NotBlank(message = "MiddleName is mandatory")
    String middleName;
    @Schema(description = "User second name", example = "Griffin")
    @NotBlank(message = "SecondName is mandatory")
    String secondName;
}
