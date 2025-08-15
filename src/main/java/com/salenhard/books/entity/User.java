package com.salenhard.books.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends UserSuperClass implements Serializable {
    @Embedded
    private UserNames userNames;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Book> bookList;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userNames, user.userNames) && Objects.equals(bookList, user.bookList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNames, bookList);
    }
}
