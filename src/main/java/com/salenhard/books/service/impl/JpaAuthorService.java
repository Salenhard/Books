package com.salenhard.books.service.impl;

import com.salenhard.books.entity.Author;
import com.salenhard.books.repository.AuthorRepository;
import com.salenhard.books.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaAuthorService implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public List<Author> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Author getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Author with id:%d is not found!".formatted(id)));
    }

    @Override
    public Author save(Author author) {
        return repository.save(author);
    }

    @Override
    public Author update(Long id, Author author) {
        Author newAuthor = repository.findById(id)
                .map(updateAuthor -> {
                            updateAuthor.setName(author.getName());
                            updateAuthor.setMiddleName(author.getMiddleName());
                            updateAuthor.setSecondName(author.getSecondName());
                            return updateAuthor;
                        }
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Author with id:%d is not found!".formatted(id)));
        return repository.save(newAuthor);
    }
}
