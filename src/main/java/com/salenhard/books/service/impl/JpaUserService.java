package com.salenhard.books.service.impl;


import com.salenhard.books.entity.User;
import com.salenhard.books.repository.UserRepository;
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
public class JpaUserService implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    @CacheEvict(value = "users", key = "#user.id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with id:%d is not found!".formatted(id)));
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User update(Long id, User user) {
        User newUser = repository.findById(id)
                .map(updateUser -> {
                            updateUser.setUserNames(user.getUserNames());
                            updateUser.setPassword(user.getPassword());
                            updateUser.setUsername(user.getUsername());
                            return updateUser;
                        }
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with id:%d is not found!".formatted(id)));
        return repository.save(newUser);
    }
}
