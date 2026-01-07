package com.example.join;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
