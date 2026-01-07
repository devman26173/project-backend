package com.example.join.entity;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int price;

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }
	
	public void change(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }
	
	public void update(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }
}