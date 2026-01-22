package com.example.join.entity;

import jakarta.persistence.*;

@Entity
public class User {
// public class Book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String region;

    public Long getId() {
		  return id;
    }
    public void setId(Long id) {
	    this.id = id;
    }
    public String getPassword() {
	    return password;
    }
    public void setPassword(String password){
	    this.password = password;
    }
    public String getRegion() {
    	return region;
    }
    public void setRegion(String region) {
    	this.region = region;
    }
}