package com.example.productservice.repository;

import com.example.productservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a.user.userId FROM Account a WHERE a.userName = :userName")
    int getUserIdByUserName(@Param("userName") String userName);
}
