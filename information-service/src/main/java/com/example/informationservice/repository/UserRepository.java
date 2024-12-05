package com.example.informationservice.repository;

import com.example.informationservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    User findByuserName(String userName);

    @Query("SELECT u FROM User u WHERE u.account.role.roleId = 3")
    List<User> getListGuest();


    @Query("SELECT u FROM User u WHERE u.email = :email AND u.account.userName = :username")
    Optional<User> getUserByEmail(@Param("email") String email, @Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, " +
            "u.gender = :gender WHERE u.userName = :userName")
    void updateInformation(@Param("userName") String userName, @Param("firstName") String firstName, @Param("lastName") String lastName,
                           @Param("gender") int gender);
}
