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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors/{authorId}/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Operations with books")
public class BookController {
    private final BookService service;
    private final KafkaTemplate<String, BookWithAuthorDto> kafkaTemplate;
    private static final String KAFKA_TOPIC_NAME = "my_topic";
    private final BookMapper bookMapper;
    private final BookWithAuthorMapper bookWithAuthorMapper;

    @Operation(summary = "Delete book by id and author id", description = "Deletes book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book is removed"),
            @ApiResponse(responseCode = "404", description = "Author is not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "User id", example = "1") Long authorId,
                                        @PathVariable @Parameter(description = "Book id", example = "1") Long id) {
        service.deleteById(authorId, id);
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
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "User id", example = "1") Long authorId,
                                     @PathVariable @Parameter(description = "Book id", example = "1") Long id) {
        Book book = service.getById(authorId, id);
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
    public ResponseEntity<?> getAll(@PathVariable @Parameter(description = "User id", example = "1") Long authorId) {
        List<BookWithAuthorDto> bookList = service.getAllByAuthorId(authorId)
                .stream()
                .map(bookWithAuthorMapper::toDto)
                .toList();
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
    public ResponseEntity<?> save(@PathVariable Long authorId,
                                  @RequestBody BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        book = service.save(authorId, book);
        BookWithAuthorDto bookWithAuthorDto = bookWithAuthorMapper.toDto(book);
        kafkaTemplate.send(KAFKA_TOPIC_NAME, bookWithAuthorDto);
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
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "User id", example = "1") Long authorId,
                                    @PathVariable @Parameter(description = "User id", example = "1") Long id,
                                    @RequestBody BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        book = service.update(authorId, id, book);
        BookWithAuthorDto bookWithAuthorDto = bookWithAuthorMapper.toDto(book);
        return ResponseEntity.ok(bookWithAuthorDto);
    }

}
