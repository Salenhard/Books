package com.salenhard.books.service.impl;

import com.salenhard.books.entity.User;
import com.salenhard.books.repository.UserRepository;
import com.salenhard.books.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaUserService implements UserService {
    private final UserRepository repository;
    private final String LOG_MESSAGE_USER_NOT_FOUND = "Error user is not found ID:{}";

    @Override
    public List<User> getAll() {
        log.info("Searching for all users");
        List<User> all = repository.findAll();
        log.info("All users successfully fetched");
        return all;
    }

    @Override
    @CacheEvict(value = "users", key = "#user.id")
    public void deleteById(Long id) {
        log.info("Deleting user by id - ID:{}", id);
        repository.deleteById(id);
        log.info("User successfully deleted - ID:{}", id);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getById(Long id) {
        log.info("Searching user by id - ID:{}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn(LOG_MESSAGE_USER_NOT_FOUND, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User with id:%d is not found!".formatted(id));
                });
        log.info("User successfully fetched ID:{}", id);
        return user;
    }

    @Override
    public User getReferenceById(Long id) {
        log.info("Search user reference - ID:{}", id);
        User user = repository.getReferenceById(id);
        log.info("User reference fetched ID:{}", id);
        return user;
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User save(User user) {
        log.info("Creating new user - USERNAME:{}", user.getUsername());
        if (repository.existsByUsername(user.getUsername())) {
            log.warn("Error creating user: user with username:{} is already exists", user.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with username: %s is already exists".formatted(user.getUsername()));
        }
        user = repository.save(user);
        log.info("User successfully saved - ID:{}", user.getId());
        return user;
    }

    @Override
    public void followUser(Long followerId, Long userToFollowId) {
        log.info("User - ID:{} following user - ID:{}", followerId, userToFollowId);
        User follower = getById(followerId);
        User userToFollow = getById(userToFollowId);

        follower.follow(userToFollow);
        repository.save(follower);
        log.info("User - ID:{} successfully followed to User - ID:{}", followerId, userToFollowId);
    }

    @Override
    public void unfollowUser(Long followerId, Long userToUnfollowId) {
        log.info("User - ID:{} unfollowing user - ID:{}", followerId, userToUnfollowId);
        User follower = getById(followerId);
        User userToUnfollow = getById(userToUnfollowId);

        follower.unFollow(userToUnfollow);
        repository.save(follower);
        log.info("User - ID:{} successfully unfollowed to user - ID:{}", follower, userToUnfollowId);
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User update(Long id, User user) {
        log.info("Updating user - ID:{}", id);
        User newUser = repository.findById(id)
                .map(updateUser -> {
                            updateUser.setUserNames(user.getUserNames());
                            updateUser.setPassword(user.getPassword());
                            updateUser.setUsername(user.getUsername());
                            updateUser.setEmail(user.getEmail());
                            return updateUser;
                        }
                ).orElseThrow(() -> {
                    log.warn(LOG_MESSAGE_USER_NOT_FOUND, id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User with id:%d is not found!".formatted(id));
                });
        newUser = repository.save(newUser);
        log.info("User successfully updated - ID:{}", user.getId());
        return newUser;
    }

    @Override
    public List<User> getAllByFollowingId(Long id) {
        log.info("Searching all follows by id - ID:{}", id);
        List<User> allByFollowingId = repository.findAllByFollowing_Id(id);
        log.info("All users successfully fetched by following id - ID:{}", id);
        return allByFollowingId;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Searching for user by username - Username:{}", username);
        User user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User is not found - Username:{}", username);
                    return new UsernameNotFoundException("User with username %s is not found".formatted(username));
                });
        log.info("User successfully found - ID:{}", user.getId());
        return user;
    }


}
