package com.example.importgoodsservice.service;

import com.example.importgoodsservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public String getNameByUserName(String userName) {
        String name = employeeRepository.getNameByUserName(userName);
        return name;
    }
}
