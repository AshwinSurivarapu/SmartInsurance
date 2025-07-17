package com.smartinsure.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Default constructor
//@AllArgsConstructor // All-args constructor
public class User {
    @Id
    private String id;

    private String username;
    private String passwordHash;
    private String email;

    public User(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

}
