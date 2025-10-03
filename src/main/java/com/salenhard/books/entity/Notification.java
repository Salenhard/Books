package com.salenhard.books.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String email;
    private String bookTitle;
    private String bookId;
    private String authorName;
    private String authorId;

    public Notification(String email, Book book) {
        this.email = email;
        this.bookTitle = book.getName();
        this.bookId = book.getId().toString();
        this.authorName = book.getUser().getUserNames().getFullName();
        this.authorId = book.getUser().getId().toString();
    }
}
