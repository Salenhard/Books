package com.salenhard.books.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class BookDto {
    @Schema(description = "Book id", example = "1")
    Long id;
    @Schema(description = "Book name", example = "The witcher")
    @NotBlank(message = "Name is mandatory")
    String name;
}