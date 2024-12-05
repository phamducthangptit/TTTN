package com.example.importgoodsservice.service;

import com.example.importgoodsservice.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public int getStaffIdByUserName(String userName) {
        return staffRepository.getStaffIdByUserName(userName);
    }
}
