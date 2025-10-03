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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations with users")
@Slf4j
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
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "User id", example = "1") Long id,
                                     HttpServletRequest request) {
        log.info("GET /api/v1/users/{} - Client IP:{}", id, request.getRemoteAddr());
        User user = service.getById(id);
        log.info("User successfully fetched - ID:{}", id);
        UserDto userDto = mapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Delete user by id", description = "Removes user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User is removed")
    })
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "User id", example = "1") Long id,
                                        HttpServletRequest request) {
        log.info("DELETE /api/v1/users/{} - Client IP:{}", id, request.getRemoteAddr());
        service.deleteById(id);
        log.info("User successfully deleted - ID:{}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users", description = "Returns list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of users",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        log.info("GET /api/v1/users Client IP:{}", request.getRemoteAddr());
        List<UserDto> users = service.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        log.info("Users all successfully fetched");
        return ResponseEntity.ok(users);
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
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> save(@RequestBody UserDto authorDto, HttpServletRequest request) {
        log.info("POST /api/v1/users Client IP:{}", request.getRemoteAddr());
        User user = mapper.toEntity(authorDto);
        user = service.save(user);
        log.info("User successfully created - ID:{}", user.getId());
        authorDto = mapper.toDto(user);
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
                                    @RequestBody UserDto userDto,
                                    HttpServletRequest request) {
        log.info("PUT /api/v1/users/{} - Client IP:{}", id, request.getRemoteAddr());
        User user = mapper.toEntity(userDto);
        user = service.update(id, user);
        log.info("User successfully updated - ID:{}", user.getId());
        userDto = mapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Follow user", description = "Subscribes on user")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User is following another user"),
            @ApiResponse(responseCode = "404", description = "User is not found")})
    @PostMapping("/{id}/follow/{userToFollowId}")
    public ResponseEntity<?> follow(@PathVariable @Parameter(description = "User id", example = "1") Long id,
                                    @PathVariable @Parameter(description = "User to follow id", example = "1") Long userToFollowId,
                                    HttpServletRequest request) {
        log.info("POST /api/v1/users/{}/follow/{} - Client IP:{}", id, userToFollowId, request.getRemoteAddr());
        service.followUser(id, userToFollowId);
        log.info("User - ID:{} successfully followed user - ID:{}", id, userToFollowId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unfollow user", description = "Unsubscribes user")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User is no more following another user"),
            @ApiResponse(responseCode = "404", description = "User is not found")})
    @PostMapping("/{id}/unfollow/{userToUnfollowId}")
    public ResponseEntity<?> unfollow(@PathVariable @Parameter(description = "User id", example = "1") Long id,
                                      @PathVariable @Parameter(description = "User to unfollow id", example = "1") Long userToUnfollowId,
                                      HttpServletRequest request) {
        log.info("POST /api/v1/users/{}/unfollow/{} - Client IP:{}", id, userToUnfollowId, request.getRemoteAddr());
        service.unfollowUser(id, userToUnfollowId);
        log.info("User - ID:{} successfully unfollowed user - ID:{}", id, userToUnfollowId);
        return ResponseEntity.noContent().build();
    }
}

