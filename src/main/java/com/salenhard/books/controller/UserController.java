package com.salenhard.books.controller;

import com.salenhard.books.entity.DTO.UserDto;
import com.salenhard.books.entity.DTO.map.UserMapper;
import com.salenhard.books.entity.User;
import com.salenhard.books.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations with users")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @Operation(summary = "Get user by id", description = "Returns user if found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "User id", example = "1") Long id) {
        User user = service.getById(id);
        UserDto userDto = mapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Delete user by id", description = "Removes user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User is removed")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "User id", example = "1") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users", description = "Returns list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of users",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserDto> authors = service.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Post user", description = "Saves user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is saved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid user data",
                    content = @Content(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
    })
    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto authorDto) {
        User author = mapper.toEntity(authorDto);
        author = service.save(author);
        authorDto = mapper.toDto(author);
        return ResponseEntity.ok(authorDto);
    }

    @Operation(summary = "Put user", description = "Updates user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "User id", example = "1") Long id,
                                    @RequestBody UserDto authorDto) {
        User user = mapper.toEntity(authorDto);
        user = service.update(id, user);
        authorDto = mapper.toDto(user);
        return ResponseEntity.ok(authorDto);
    }

}

