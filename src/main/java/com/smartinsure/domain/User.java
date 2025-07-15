package com.smartinsure.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Default constructor
@AllArgsConstructor // All-args constructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // Auto-generates ID for PostgreSQL
    private Long id;

    @Column(nullable=false,unique=true)// Column constraints: cannot be null, must be unique
    private String username;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false,unique=true)
    private String email;


}
