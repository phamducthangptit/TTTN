package com.example.productservice.service;

import com.example.productservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    public int getUserId(String userName){
        return repository.getUserIdByUserName(userName);
    }
}
