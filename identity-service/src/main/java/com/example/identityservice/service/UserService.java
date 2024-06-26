package com.example.identityservice.service;

import com.example.identityservice.entity.User;
import com.example.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
