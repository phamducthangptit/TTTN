package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT CONCAT(s.firstName,' ', s.lastName) FROM Staff s WHERE s.account.userName = :userName")
    public String getNameByUserName(@Param("userName") String userName);
}
