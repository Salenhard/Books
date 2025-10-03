package com.salenhard.books.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends UserSuperClass implements Serializable, UserDetails {
    @Embedded
    private UserNames userNames;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> bookList;
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles;
    @ManyToMany
    @JoinTable(name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> following = new ArrayList<>();
    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userNames, user.userNames) && Objects.equals(bookList, user.bookList);
    }

    public void follow(User userToFollow) {
        if (!following.contains(userToFollow)) {
            following.add(userToFollow);
            userToFollow.getFollowers().add(this);
        }
    }

    public void unFollow(User userToUnfollow) {
        if (following.remove(userToUnfollow)) {
            userToUnfollow.getFollowers().remove(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", username=" + super.getUsername() +
                ", password=" + super.getPassword() +
                ", userNames=" + userNames +
                ", bookList=" + bookList +
                ", roles=" + roles +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNames, bookList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
