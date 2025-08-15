package com.salenhard.books.service.impl;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.User;
import com.salenhard.books.repository.BookRepository;
import com.salenhard.books.service.BookService;
import com.salenhard.books.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaBookService implements BookService {
    private final BookRepository repository;
    private final UserService userService;

    @Override
    @CachePut(value = "books", key = "#book.id")
    public Book save(Long userId, Book book) {
        User user = userService.getById(userId);
        book.setUser(user);
        return repository.save(book);
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    public void deleteById(Long userId, Long id) {
        repository.deleteById(id);
    }

    @Override
    @CachePut(value = "books", key = "#id")
    public Book update(Long userId, Long id, Book book) {
        User author = userService.getById(userId);
        Book updateBook = repository.findById(id).map(newBook -> {
            newBook.setName(book.getName());
            newBook.setUser(author);
            return newBook;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id %d is not found!".formatted(id)));
        return repository.save(updateBook);
    }

    @Override
    public List<Book> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Book> getAllByAuthorId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public Book getById(Long userId, Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book with id %d is not found!".formatted(id)));
    }
}
