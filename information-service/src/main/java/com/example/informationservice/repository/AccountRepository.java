package com.example.informationservice.repository;

import com.example.informationservice.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.status = :status WHERE a.accountId = :id")
    void updateStatusAccount(@Param("status") int status, @Param("id") int id);

    Optional<Account> findByuserName(String username);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.password = :password WHERE a.userName = :userName")
    void updatePassword(@Param("password") String password, @Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.password = :password WHERE a.user.email = :email")
    void resetPasswordUser(@Param("password") String password, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.password = :password WHERE a.staff.email = :email")
    void resetPasswordStaff(@Param("password") String password, @Param("email") String email);

    @Query("SELECT a.user.userId FROM Account a WHERE a.userName = :userName")
    int getUserIdByUserName(@Param("userName") String userName);
}
