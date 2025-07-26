package com.salenhard.books.service;

import com.salenhard.books.entity.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAll();

    void deleteById(Long id);

    Author getById(Long id);

    Author save(Author author);

    Author update(Long id, Author author);

}
