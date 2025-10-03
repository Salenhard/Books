package com.salenhard.books.entity.DTO;

import com.salenhard.books.entity.UserNames;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto extends UserSuperClassDto {
    @Schema(description = "User name", example = "John")
    @NotBlank(message = "Name is mandatory")
    String name;
    @Schema(description = "User middle name", example = "Rick")
    @NotBlank(message = "MiddleName is mandatory")
    String middleName;
    @Schema(description = "User second name", example = "Griffin")
    @NotBlank(message = "SecondName is mandatory")
    String secondName;
    List<String> roles;
}