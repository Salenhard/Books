package com.salenhard.books.controller;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.DTO.BookDto;
import com.salenhard.books.entity.DTO.BookWithAuthorDto;
import com.salenhard.books.entity.DTO.map.BookMapper;
import com.salenhard.books.entity.DTO.map.BookWithAuthorMapper;
import com.salenhard.books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Operations with books")
@Slf4j
public class BookController {
    private final BookService service;
    private final BookMapper bookMapper;
    private final BookWithAuthorMapper bookWithAuthorMapper;

    @Operation(summary = "Delete book by id and author id", description = "Deletes book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book is removed"),
            @ApiResponse(responseCode = "404", description = "Author is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "User id", example = "1") Long userId,
                                        @PathVariable @Parameter(description = "Book id", example = "1") Long id,
                                        HttpServletRequest request) {
        log.info("DELETE /api/v1/users/{}/{} - Client IP:{}", userId, id, request.getRemoteAddr());
        service.deleteById(userId, id);
        log.info("Book deleted successfully - ID:{}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get book by id and author id", description = "Returns book with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book is found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "404", description = "Author or book is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "User id", example = "1") Long userId,
                                     @PathVariable @Parameter(description = "Book id", example = "1") Long id,
                                     HttpServletRequest request) {
        log.info("POST /api/v1/users/{}/books/{} - Client IP: {}", userId, id, request.getRemoteAddr());
        Book book = service.getById(userId, id);
        log.info("Book successfully fetched - ID:{}", id);
        BookWithAuthorDto bookDto = bookWithAuthorMapper.toDto(book);
        return ResponseEntity.ok(bookDto);
    }

    @Operation(summary = "Get all books by author id", description = "Return list of all books of author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books of author is found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookWithAuthorDto.class)))),
            @ApiResponse(responseCode = "404", description = "Author is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable @Parameter(description = "User id", example = "1") Long userId,
                                    HttpServletRequest request) {
        log.info("GET /api/v1/users/{}/books - Client IP: {}", userId, request.getRemoteAddr());
        List<BookWithAuthorDto> bookList = service.getAllByUserId(userId)
                .stream()
                .map(bookWithAuthorMapper::toDto)
                .toList();
        log.info("Books successfully fetched by user id - ID:{}", userId);
        return ResponseEntity.ok(bookList);
    }

    @Operation(summary = "Post book with author id", description = "Saves book with author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book is saved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookWithAuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Author is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
    })
    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long userId,
                                  @Valid @RequestBody BookDto bookDto,
                                  HttpServletRequest request) {
        log.info("POST /api/v1/users/{}/books - Client IP:{}", userId, request.getRemoteAddr());
        Book book = bookMapper.toEntity(bookDto);
        book = service.save(userId, book);
        log.info("Book created successfully - ID:{}", book.getId());
        BookWithAuthorDto bookWithAuthorDto = bookWithAuthorMapper.toDto(book);
        return ResponseEntity.ok(bookWithAuthorDto);
    }

    @Operation(summary = "Put book with author id", description = "Updates book with author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book is updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookWithAuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Author or Book is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "User id", example = "1") Long userId,
                                    @PathVariable @Parameter(description = "Book id", example = "1") Long id,
                                    @Valid @RequestBody BookDto bookDto,
                                    HttpServletRequest request) {
        log.info("PUT /api/v1/users/{}/books/{} - Client IP:{}", userId, id, request.getRemoteAddr());
        Book book = bookMapper.toEntity(bookDto);
        book = service.update(userId, id, book);
        log.info("Book successfully updated - ID:{}", book.getId());
        BookWithAuthorDto bookWithAuthorDto = bookWithAuthorMapper.toDto(book);
        return ResponseEntity.ok(bookWithAuthorDto);
    }

}
