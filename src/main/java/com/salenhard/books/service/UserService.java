package com.salenhard.books.service;

import com.salenhard.books.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    void deleteById(Long id);

    User getById(Long id);

    User save(User author);

    User update(Long id, User author);

}
