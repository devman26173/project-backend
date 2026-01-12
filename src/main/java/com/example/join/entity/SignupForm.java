package com.example.join.entity;

import jakarta.persistence.*;

@Entity
public class SignupForm {
// public class Book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String content;

    public Long getId() {
		  return Id;
    }
    public void setId(Long Id) {
	    this.Id = Id;
    }
    public String content() {
	    return content;
    }
    public void setContent(String content){
	    this.content = content;
    }    
}