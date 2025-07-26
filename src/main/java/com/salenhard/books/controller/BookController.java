package com.salenhard.books.controller;

import com.salenhard.books.entity.Book;
import com.salenhard.books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors/{authorId}/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String KAFKA_TOPIC_NAME = "my_topic";

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long authorId,
                                        @PathVariable Long id) {
        service.deleteById(authorId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long authorId,
                                     @PathVariable Long id) {
        Book book = service.getById(authorId, id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable Long authorId) {
        List<Book> bookList = service.getAllByAuthorId(authorId);
        return ResponseEntity.ok(bookList);
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long authorId,
                                  @RequestBody Book book) {
        book = service.save(authorId, book);
        kafkaTemplate.send(KAFKA_TOPIC_NAME, book.toString());
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long authorId,
                                    @PathVariable Long id,
                                    @RequestBody Book book) {
        book = service.update(authorId, id, book);
        return ResponseEntity.ok(book);
    }

}
