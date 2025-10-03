package com.salenhard.books.controller;

import com.salenhard.books.entity.DTO.SignInRequest;
import com.salenhard.books.entity.DTO.UserDto;
import com.salenhard.books.entity.DTO.map.UserMapper;
import com.salenhard.books.entity.User;
import com.salenhard.books.service.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService service;
    private final UserMapper mapper;

    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public String login(@RequestBody @Valid SignInRequest signInRequest,
                        HttpServletRequest request) {
        log.info("POST /api/v1/auth - Client IP: {}", request.getRemoteAddr());
        String jwt = service.signIn(signInRequest);
        log.info("User successfully sign in {}", signInRequest.getUsername());
        return jwt;
    }

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is registered",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "User with this username is already exists",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto userDto,
                                      HttpServletRequest request) {
        log.info("POST /api/v1/register - Client IP: {}", request.getRemoteAddr());
        User user = mapper.toEntity(userDto);
        userDto = mapper.toDto(service.signUp(user));
        log.info("User created successfully - ID: {}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
