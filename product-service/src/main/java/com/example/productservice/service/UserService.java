package com.example.productservice.service;

import com.example.productservice.entity.User;
import com.example.productservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User getUserById(int userId){
        return repository.findByuserId(userId);
    }
}
