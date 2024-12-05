package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT s.id FROM Staff s WHERE s.account.userName = :userName")
    int getStaffIdByUserName(@Param("userName") String username);
}
