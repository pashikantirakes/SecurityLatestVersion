package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User> findByName(String name);
 boolean existsByName(String name);
 boolean existsByEmail(String email);
}
