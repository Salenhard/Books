package com.salenhard.books.service;

import com.salenhard.books.entity.Book;

import java.util.List;

public interface BookService {

    Book save(Long authorId, Book book);

    void deleteById(Long authorId, Long id);

    Book update(Long authorId, Long id, Book book);

    List<Book> getAll();

    List<Book> getAllByAuthorId(Long authorId);

    Book getById(Long authorId, Long id);
}
