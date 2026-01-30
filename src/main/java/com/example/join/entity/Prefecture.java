package com.example.join.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "prefectures")
@Entity
public class Prefecture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String Prefecture;
	
	public Long getId() {
		return id;
	}
	public String getPrefecture() {
		return prefecture;
	}
}
