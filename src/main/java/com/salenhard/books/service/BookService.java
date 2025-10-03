package com.salenhard.books.service;

import com.salenhard.books.entity.Book;

import java.util.List;

public interface BookService {

    Book save(Long userId, Book book);

    void deleteById(Long userId, Long id);

    Book update(Long userId, Long id, Book book);

    List<Book> getAll();

    List<Book> getAllByUserId(Long userId);

    Book getById(Long userId, Long id);
}
