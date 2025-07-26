package com.salenhard.books.service.impl;

import com.salenhard.books.entity.Author;
import com.salenhard.books.entity.Book;
import com.salenhard.books.repository.BookRepository;
import com.salenhard.books.service.AuthorService;
import com.salenhard.books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaBookService implements BookService {
    private final BookRepository repository;
    private final AuthorService authorService;

    @Override
    public Book save(Long authorId, Book book) {
        Author author = authorService.getById(authorId);
        book.setAuthor(author);
        return repository.save(book);
    }

    @Override
    public void deleteById(Long authorId, Long id) {
        repository.deleteById(id);
    }

    @Override
    public Book update(Long authorId, Long id, Book book) {
        Author author = authorService.getById(authorId);
        Book updateBook = repository.findById(id).map(newBook -> {
            newBook.setName(book.getName());
            newBook.setAuthor(author);
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
    public List<Book> getAllByAuthorId(Long authorId) {
        return repository.findAllByAuthorId(authorId);
    }

    @Override
    public Book getById(Long authorId, Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book with id %d is not found!".formatted(id)));
    }
}
