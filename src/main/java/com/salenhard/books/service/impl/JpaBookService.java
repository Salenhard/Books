package com.salenhard.books.service.impl;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.Notification;
import com.salenhard.books.entity.User;
import com.salenhard.books.repository.BookRepository;
import com.salenhard.books.service.BookService;
import com.salenhard.books.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaBookService implements BookService {
    private final BookRepository repository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @Override
    @CachePut(value = "books", key = "#book.id")
    public Book save(Long userId, Book book) {
        log.info("Creating book for user - UserID:{}", userId);
        User user = userService.getReferenceById(userId);
        book.setUser(user);
        book = repository.save(book);
        for (User follower : userService.getAllByFollowingId(userId))
            kafkaProducerService.sendMessage(new Notification(follower.getEmail(), book));
        log.info("Book created successfully - ID:{}", book.getId());
        return book;
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    public void deleteById(Long userId, Long id) {
        log.info("Deleting book - ID:{}", id);
        repository.deleteById(id);
        log.info("Book deleted successfully - ID:{}", id);
    }

    @Override
    @CachePut(value = "books", key = "#id")
    public Book update(Long userId, Long id, Book book) {
        log.info("Updating book - ID:{}", id);
        User user = userService.getReferenceById(userId);
        Book updateBook = repository.findById(id).map(newBook -> {
            newBook.setName(book.getName());
            newBook.setUser(user);
            return newBook;
        }).orElseThrow(() -> {
            log.warn("Error book is not found - ID:{}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book with id %d is not found!".formatted(id));
        });
        updateBook = repository.save(updateBook);
        log.info("Book successfully updated - ID:{}", id);
        return updateBook;
    }

    @Override
    public List<Book> getAll() {
        log.info("Fetching all books");
        List<Book> all = repository.findAll();
        log.info("All books fetched");
        return all;
    }

    @Override
    public List<Book> getAllByUserId(Long userId) {
        log.info("Searching for all user books - UserID:{}", userId);
        List<Book> allByUserId = repository.findAllByUserId(userId);
        log.info("All user books fetched - UserID:{}", userId);
        return allByUserId;
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public Book getById(Long userId, Long id) {
        log.info("Fetching book by id - ID:{}", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book is not found - ID:{}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Book with id %d is not found!".formatted(id));
                });
        log.info("Book successfully found - ID:{}", id);
        return book;
    }
}
