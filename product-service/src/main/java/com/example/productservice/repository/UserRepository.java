package com.example.productservice.repository;

import com.example.productservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByuserId(int userId);
}
