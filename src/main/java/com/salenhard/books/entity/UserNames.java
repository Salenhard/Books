package com.salenhard.books.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserNames implements Serializable {
    private String name;
    private String middleName;
    private String secondName;

    public String getFullName() {
        return middleName + " " + secondName.toUpperCase().charAt(0) + "." + name.toUpperCase().charAt(0) + ".";
    }

    @Override
    public String toString() {
        return "UserNames{" +
                "name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
