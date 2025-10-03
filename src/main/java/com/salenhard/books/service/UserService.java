package com.salenhard.books.service;

import com.salenhard.books.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();

    User getReferenceById(Long id);

    void deleteById(Long id);

    User getById(Long id);

    User save(User user);

    void followUser(Long followerId, Long userToFollowId);

    void unfollowUser(Long followerId, Long userToUnfollowId);

    User update(Long id, User user);

    List<User> getAllByFollowingId(Long id);
}
