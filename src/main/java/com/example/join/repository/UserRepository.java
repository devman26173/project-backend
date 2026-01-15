package com.example.join.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
