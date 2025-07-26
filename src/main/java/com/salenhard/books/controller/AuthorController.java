package com.salenhard.books.controller;

import com.salenhard.books.entity.Author;
import com.salenhard.books.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private AuthorService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Author author = service.getById(id);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Author> authors = service.getAll();
        return ResponseEntity.ok(authors);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Author author) {
        author = service.save(author);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Author author) {
        author = service.update(id, author);
        return ResponseEntity.ok(author);
    }

}

