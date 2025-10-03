package com.salenhard.books.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithAuthorDto {
    @Schema(description = "Book id", example = "1")
    Long id;
    @Schema(description = "Book name", example = "The witcher")
    String name;
    @Schema(description = "Author", exampleClasses = UserDto.class)
    UserDto user;
}
