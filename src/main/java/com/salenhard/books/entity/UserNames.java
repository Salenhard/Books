package com.salenhard.books.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserNames {
    private String name;
    private String middleName;
    private String secondName;
}
